package com.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.cos.COSName;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExtractImages {

    public static void extractImages(PDDocument document, File tempDir) throws IOException {
        int imageCount = 0;
        for (PDPage page : document.getPages()) {
            int pageNumber = page.getCOSObject().getInt(COSName.PAGE);
            PDResources resources = page.getResources();
            for (COSName cosName : resources.getXObjectNames()) {
                try {
                    PDXObject xobject = resources.getXObject(cosName);
                    if (xobject instanceof PDImageXObject) {
                        PDImageXObject imageObject = (PDImageXObject) xobject;
                        BufferedImage image = imageObject.getImage();
                        String filename = String.format("%04d_%d.png", pageNumber, imageCount++); 
                        File imageFile = new File(tempDir, filename);
                        ImageIO.write(image, "png", imageFile);
                    }
                } catch (IOException e) {
                    System.err.println("Error extracting image: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ExtractImages <input.pdf> <output_directory>");
            System.exit(1);
        }

        try (PDDocument document = PDDocument.load(new File(args[0]))) {
            File tempDir = new File(args[1]);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            extractImages(document, tempDir);
        } catch (IOException e) {
            System.err.println("Error processing PDF: " + e.getMessage());
            System.exit(1);
        }
    }
}