import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.DataBufferFloat;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.Raster;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.BufferedImage;
import java.awt.Transparency;

public class FloatImage {
    public static BufferedImage CreateFloatImage(int width,int height) {
        // Define dimensions and layout of the image
        int w = width;
        int h = height;
        int bands = 1; // 4 bands for ARGB, 3 for RGB etc
        int[] bandOffsets = {0}; // length == bands, 0 == R, 1 == G, 2 == B and 3 == A

        // Create a TYPE_FLOAT sample model (specifying how the pixels are stored)
        SampleModel sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_FLOAT, w, h, bands, w  * bands, bandOffsets);
        // ...and data buffer (where the pixels are stored)
        DataBuffer buffer = new DataBufferFloat(w * h * bands);

        // Wrap it in a writable raster
        WritableRaster raster = Raster.createWritableRaster(sampleModel, buffer, null);

        // Create a color model compatible with this sample model/raster (TYPE_FLOAT)
        // Note that the number of bands must equal the number of color components in the 
        // color space (3 for RGB) + 1 extra band if the color model contains alpha 
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorModel colorModel = new ComponentColorModel(colorSpace, false, false, Transparency.OPAQUE, DataBuffer.TYPE_FLOAT);

        // And finally create an image with this raster
        BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
        float test = 0.34f;
        raster.setDataElements(5, 5, test);

        return image;
    }
}