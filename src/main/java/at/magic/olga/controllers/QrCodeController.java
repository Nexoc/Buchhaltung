package at.magic.olga.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@RestController
public class QrCodeController {

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateQr(HttpServletResponse response) {
        try {
            String ip = getLocalIp();
            String url = "http://" + ip + ":8000"; // подставь свой порт

            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);

            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            OutputStream out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            out.flush();
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    private String getLocalIp() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp()) continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr.getHostAddress().contains(".")) {
                    return addr.getHostAddress();
                }
            }
        }
        throw new RuntimeException("Local IP not found");
    }
}
