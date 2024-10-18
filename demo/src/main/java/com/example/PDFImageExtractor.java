package com.example;


import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PDFImageExtractor {

    public static void extractImages(File pdfFile, File tempDir) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            int imageCount = 0;
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                PDPage pdPage = document.getPage(page);
                PDResources resources = pdPage.getResources();

                for (COSName cosName : resources.getXObjectNames()) {
                    try {
                        PDXObject xobject = resources.getXObject(cosName);
                        if (xobject instanceof PDImageXObject) {
                            PDImageXObject imageObject = (PDImageXObject) xobject;
                            BufferedImage image = imageObject.getImage();
                            String filename =  pdfFile.getName().replace(".pdf", "") + "_page_" + (page + 1) + "_image_" + UUID.randomUUID() + ".png";
                            File imageFile = new File(tempDir, filename);
                            ImageIO.write(image, "png", imageFile);
                            imageCount++;
                        }
                    } catch (IOException e) {
                        System.err.println("Error extracting image: " + e.getMessage());
                    }
                }
            }
            System.out.println("Extracted " + imageCount + " images.");
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java PDFImageExtractor <pdf_file> <output_directory>");
            System.exit(1);
        }

        File pdfFile = new File(args[0]);
        File tempDir = new File(args[1]);

        try {
            extractImages(pdfFile, tempDir);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}