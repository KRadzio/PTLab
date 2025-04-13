package rp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;

public class App {
    public static void main(String[] args) {
        var pictureInDir = getPictureInDir(args.length == 2 ? args[0] : null);
        var pictureOutDir = getPictureOutDir(args.length == 2 ? args[1] : null);
        var inputPictures = getInputPictures(pictureInDir);

        // standard
        var startTime = System.currentTimeMillis();
        inputPictures.stream()
                .map(pictureFile -> readImage(pictureFile))
                .map(nameAndImage -> transformImage(nameAndImage))
                .forEach(nameAndImage -> saveImage(nameAndImage, pictureOutDir));
        System.out.println(String.format("Standard stream time: %d ms", System.currentTimeMillis() - startTime));

        // parallel
        startTime = System.currentTimeMillis();
        inputPictures.parallelStream()
                .map(pictureFile -> readImage(pictureFile))
                .map(nameAndImage -> transformImage(nameAndImage))
                .forEach(nameAndImage -> saveImage(nameAndImage, pictureOutDir));
        System.out.println(String.format("Parallel stream time: %d ms", System.currentTimeMillis() - startTime));

        // custom parallel
        var poolSize = 2;
        ForkJoinPool customThreadPool = new ForkJoinPool(poolSize);
        startTime = System.currentTimeMillis();
        customThreadPool.submit(() -> inputPictures.parallelStream()
                .map(pictureFile -> readImage(pictureFile))
                .map(nameAndImage -> transformImage(nameAndImage))
                .forEach(nameAndImage -> saveImage(nameAndImage, pictureOutDir))).invoke();
        System.out.println(String.format("Custom parallel stream with pool size: %d, time: %d ms", poolSize,
                System.currentTimeMillis() - startTime));
        customThreadPool.shutdown();
    }

    private static Path getPictureInDir(String pictureInDirName) {
        return pictureInDirName != null
                ? Path.of(pictureInDirName)
                : Path.of("/home/rat/studia/PT/lab6/pictureIn");
    }

    private static Path getPictureOutDir(String pictureOutDirName) {
        return pictureOutDirName != null
                ? Path.of(pictureOutDirName)
                : Path.of("/home/rat/studia/PT/lab6/pictureOut");
    }

    private static List<Path> getInputPictures(Path pictureInDir) {
        try {
            return Files.list(pictureInDir).toList();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Pair<String, BufferedImage> readImage(Path pictureFile) {
        try {
            var image = ImageIO.read(pictureFile.toFile());
            var name = pictureFile.getFileName().toString();
            return Pair.of(name, image);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Pair<String, BufferedImage> transformImage(Pair<String, BufferedImage> nameAndImage) {
        var image = nameAndImage.getValue();
        // inverse colors
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                Color color = new Color(rgb);
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                Color outColor = new Color(blue, red, green);
                int outRgb = outColor.getRGB();
                image.setRGB(i, j, outRgb);
            }
        }
        return nameAndImage;
    }

    private static void saveImage(Pair<String, BufferedImage> nameAndImage, Path pictureOutDir) {
        try {
            var name = nameAndImage.getKey();
            var image = nameAndImage.getValue();
            ImageIO.write(image, "jpg", pictureOutDir.resolve(name).toFile());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
