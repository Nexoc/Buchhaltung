-- ******************************
-- Schema Definition for Olga Application
-- Generated from JPA entity models
-- Includes tables and two views (not materialized)
-- ******************************

-- ******************************
-- Table: card_expenses
-- Represents card expense records
-- ******************************
CREATE TABLE IF NOT EXISTS card_expenses (
  id SERIAL PRIMARY KEY,                                  -- Unique identifier for each card expense
  expense_time TIMESTAMP WITH TIME ZONE,                   -- Timestamp when the expense occurred
  amount NUMERIC(15,2),                                   -- Amount charged on the card (precision for currency)
  description VARCHAR(255)                                -- Brief description or note about the expense
);

-- ******************************
-- Table: cash_expenses
-- Represents cash transaction records
-- ******************************
CREATE TABLE IF NOT EXISTS cash_expenses (
  id SERIAL PRIMARY KEY,                                  -- Unique identifier for each cash transaction
  expense_time TIMESTAMP WITH TIME ZONE NOT NULL,          -- Timestamp when the cash movement occurred (mandatory)
  amount NUMERIC(15,2),                                   -- Amount of cash movement (precision for currency)
  description VARCHAR(255)                                -- Brief description or note
);

-- ******************************
-- Table: categories
-- Represents product categories
-- ******************************
CREATE TABLE IF NOT EXISTS categories (
  id SERIAL PRIMARY KEY,                                  -- Unique category identifier
  name VARCHAR(255),                                      -- Category name
  description VARCHAR(255)                                -- Category description
);

-- ******************************
-- Table: products
-- Represents inventory items
-- ******************************
CREATE TABLE IF NOT EXISTS products (
  id SERIAL PRIMARY KEY,                                  -- Unique product identifier
  name VARCHAR(255),                                      -- Product name
  price NUMERIC(15,2),                                    -- Product price (precision for currency)
  stock INTEGER,                                          -- Quantity in stock
  category_id INTEGER NOT NULL                            -- Foreign key to categories
    REFERENCES categories(id)                             -- Enforce referential integrity
    ON DELETE CASCADE,                                    -- Delete products if category is removed
  added_date DATE,                                        -- Date when product was added
  image_path VARCHAR(255),                                -- File system path or URL of product image
  description TEXT                                        -- Detailed product description (no length limit)
);

-- ******************************
-- Table: instructions
-- Represents instruction files linked to products
-- ******************************
CREATE TABLE IF NOT EXISTS instructions (
  id SERIAL PRIMARY KEY,                                  -- Unique instruction file identifier
  filename VARCHAR(255),                                  -- Name of the instruction file
  description VARCHAR(255),                               -- Description or notes about the file
  product_id INTEGER NOT NULL                             -- Foreign key to products
    REFERENCES products(id)                               -- Enforce link to product
    ON DELETE CASCADE                                     -- Remove instructions if product is deleted
);

-- ******************************
-- Table: sales
-- Represents sales transactions
-- ******************************
CREATE TABLE IF NOT EXISTS sales (
  id SERIAL PRIMARY KEY,                                  -- Unique sale transaction identifier
  product_id INTEGER NOT NULL                             -- Foreign key to sold product
    REFERENCES products(id),                              -- Enforce link to product
  sale_time TIMESTAMP WITH TIME ZONE NOT NULL,            -- Timestamp of sale event
  quantity INTEGER,                                       -- Number of units sold
  payment_method VARCHAR(10) NOT NULL,                    -- Payment method: CASH or CARD
  comment TEXT                                            -- Optional comments or notes about the sale
);

-- ******************************
-- View: cash_journal
-- Provides a monthly financial overview
-- Combines income, card expenses, cash expenses, balance change, and running total
-- ******************************
CREATE OR REPLACE VIEW cash_journal AS
WITH
  monthly_income AS (
    SELECT
      DATE_TRUNC('month', s.sale_time) AS month_start,    -- First day of sale month
      SUM(s.quantity * p.price)          AS income_total  -- Total income per month
    FROM sales s
    JOIN products p ON p.id = s.product_id
    GROUP BY DATE_TRUNC('month', s.sale_time)
  ),
  monthly_card AS (
    SELECT
      DATE_TRUNC('month', expense_time) AS month_start,    -- First day of expense month
      SUM(amount)                        AS card_expenses_total -- Total card expenses per month
    FROM card_expenses
    GROUP BY DATE_TRUNC('month', expense_time)
  ),
  monthly_cash AS (
    SELECT
      DATE_TRUNC('month', expense_time) AS month_start,    -- First day of cash expense month
      SUM(amount)                        AS cash_expenses_total -- Total cash expenses per month
    FROM cash_expenses
    GROUP BY DATE_TRUNC('month', expense_time)
  )
SELECT
  ROW_NUMBER() OVER (ORDER BY mi.month_start)                                        AS id,                -- Sequential ID per month
  EXTRACT(MONTH FROM mi.month_start)::INT                                            AS month_number,      -- Month number (1-12)
  EXTRACT(YEAR FROM mi.month_start)::INT                                             AS year,              -- Year number
  COALESCE(mi.income_total, 0)                                                       AS income_total,      -- Income for the month
  COALESCE(mc.card_expenses_total, 0)                                                AS card_expenses_total,-- Card expenses for the month
  COALESCE(mca.cash_expenses_total, 0)                                               AS cash_expenses_total,-- Cash expenses for the month
  COALESCE(mi.income_total, 0)
    - COALESCE(mc.card_expenses_total, 0)
    - COALESCE(mca.cash_expenses_total, 0)                                            AS balance_change,    -- Net change for the month
  SUM(
    COALESCE(mi.income_total, 0)
    - COALESCE(mc.card_expenses_total, 0)
    - COALESCE(mca.cash_expenses_total, 0)
  ) OVER (ORDER BY mi.month_start)                                                    AS running_total      -- Cumulative running total up to this month
FROM monthly_income mi
LEFT JOIN monthly_card mc ON mc.month_start = mi.month_start
LEFT JOIN monthly_cash mca ON mca.month_start = mi.month_start
ORDER BY year, month_number;


-- ******************************
-- View: daily_pl
-- Provides daily profit and loss overview
-- Includes total sales, card expenses, cash expenses, and net profit per day
-- ******************************
CREATE OR REPLACE VIEW daily_pl AS
WITH
  daily_sales AS (
    SELECT
      DATE(s.sale_time)              AS day,               -- Specific calendar day
      SUM(s.quantity * p.price)       AS sales_amount      -- Total sales revenue that day
    FROM sales s
    JOIN products p ON p.id = s.product_id
    GROUP BY DATE(s.sale_time)
  ),
  daily_card AS (
    SELECT
      DATE(expense_time)             AS day,               -- Specific calendar day
      SUM(amount)                    AS card_expenses      -- Total card expenses that day
    FROM card_expenses
    GROUP BY DATE(expense_time)
  ),
  daily_cash AS (
    SELECT
      DATE(expense_time)             AS day,               -- Specific calendar day
      SUM(amount)                    AS cash_expenses      -- Total cash expenses that day
    FROM cash_expenses
    GROUP BY DATE(expense_time)
  )
SELECT
  ROW_NUMBER() OVER (ORDER BY ds.day)     AS id,             -- Sequential ID per day
  ds.day                                  AS day,            -- Date of the record
  COALESCE(ds.sales_amount, 0)            AS sales_amount,   -- Sales revenue for the day
  COALESCE(dc.card_expenses, 0)           AS card_expenses,  -- Card expenses for the day
  COALESCE(dca.cash_expenses, 0)          AS cash_expenses,  -- Cash expenses for the day
  COALESCE(ds.sales_amount, 0)
    - COALESCE(dc.card_expenses, 0)
    - COALESCE(dca.cash_expenses, 0)       AS net_profit,     -- Net profit (sales minus expenses)
  ''                                      AS description      -- Placeholder for optional notes or comments
FROM daily_sales ds
LEFT JOIN daily_card dc ON dc.day = ds.day
LEFT JOIN daily_cash dca ON dca.day = ds.day
ORDER BY ds.day;


-- ******************************
-- View: weekly_pl (Weekly Profit & Loss)
-- ******************************
CREATE OR REPLACE VIEW weekly_pl AS
WITH
  weekly_sales AS (
    /* Aggregated sales by ISO week */
    SELECT
      DATE_TRUNC('week', s.sale_time)::DATE AS week_start,
      SUM(s.quantity * p.price)             AS sales_amount
    FROM sales s
    JOIN products p ON p.id = s.product_id
    GROUP BY DATE_TRUNC('week', s.sale_time)
  ),
  weekly_card AS (
    /* Aggregated card expenses by ISO week */
    SELECT
      DATE_TRUNC('week', expense_time)::DATE AS week_start,
      SUM(amount)                            AS card_expenses
    FROM card_expenses
    GROUP BY DATE_TRUNC('week', expense_time)
  ),
  weekly_cash AS (
    /* Aggregated cash expenses by ISO week */
    SELECT
      DATE_TRUNC('week', expense_time)::DATE AS week_start,
      SUM(amount)                            AS cash_expenses
    FROM cash_expenses
    GROUP BY DATE_TRUNC('week', expense_time)
  )
SELECT
  ROW_NUMBER() OVER (ORDER BY ws.week_start)     AS id,
  ws.week_start,
  COALESCE(ws.sales_amount,0)                   AS sales_amount,
  COALESCE(wc.card_expenses,0)                  AS card_expenses,
  COALESCE(wca.cash_expenses,0)                 AS cash_expenses,
  COALESCE(ws.sales_amount,0)
    - COALESCE(wc.card_expenses,0)
    - COALESCE(wca.cash_expenses,0)              AS net_profit
FROM weekly_sales ws
LEFT JOIN weekly_card wc  ON wc.week_start  = ws.week_start
LEFT JOIN weekly_cash wca ON wca.week_start = ws.week_start
ORDER BY ws.week_start;

-- ******************************
-- View: yearly_pl (Yearly Profit & Loss)
-- ******************************
CREATE OR REPLACE VIEW yearly_pl AS
WITH
  yearly_sales AS (
    SELECT
      EXTRACT(YEAR FROM s.sale_time)::INT AS year,
      SUM(s.quantity * p.price)            AS sales_amount
    FROM sales s
    JOIN products p ON p.id = s.product_id
    GROUP BY EXTRACT(YEAR FROM s.sale_time)
  ),
  yearly_card AS (
    SELECT
      EXTRACT(YEAR FROM expense_time)::INT AS year,
      SUM(amount)                          AS card_expenses    -- имя изменено
    FROM card_expenses
    GROUP BY EXTRACT(YEAR FROM expense_time)
  ),
  yearly_cash AS (
    SELECT
      EXTRACT(YEAR FROM expense_time)::INT AS year,
      SUM(amount)                          AS cash_expenses    -- имя изменено
    FROM cash_expenses
    GROUP BY EXTRACT(YEAR FROM expense_time)
  )
SELECT
  ys.year,
  COALESCE(ys.sales_amount,0)    AS sales_amount,
  COALESCE(yc.card_expenses,0)   AS card_expenses,   -- имя под ваш entity
  COALESCE(yca.cash_expenses,0)  AS cash_expenses,   -- имя под ваш entity
  COALESCE(ys.sales_amount,0)
    - COALESCE(yc.card_expenses,0)
    - COALESCE(yca.cash_expenses,0) AS net_profit
FROM yearly_sales ys
LEFT JOIN yearly_card yc  ON yc.year  = ys.year
LEFT JOIN yearly_cash yca ON yca.year = ys.year
ORDER BY ys.year;


-- ******************************
-- View: annual_summary (Aggregated across all years)
-- ******************************
CREATE OR REPLACE VIEW annual_summary AS
SELECT
  1 AS id,
  SUM(s.quantity * p.price)       AS total_sales,
  SUM(c1.amount)                   AS total_card_expenses,
  SUM(c2.amount)                   AS total_cash_expenses,
  SUM(s.quantity * p.price)
    - SUM(c1.amount)
    - SUM(c2.amount)               AS total_net_profit
FROM sales s
JOIN products p ON p.id = s.product_id
LEFT JOIN card_expenses c1 ON TRUE
LEFT JOIN cash_expenses c2 ON TRUE;
