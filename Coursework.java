import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/* Student Number - 905010
 * Student Name - Benjamin Thomas
 * 
 * All work is my own and nobody else's. I have used the starting template
 * and methods from the given Java file 'Example.java' from the course notes 
 * that I altered appropriately to include my own working solutions for this 
 * course work.
 */

public class Coursework extends JFrame {
	static final int HEIGHT = 113;
	static final int WIDTH = 256;
	static final int LENGTH = 256;
	Container resizeTopContainer;
	Container resizeSideContainer;
	Container resizeFrontContainer;

	JButton[] topSelectThumbnailButton;
	JButton[] sideSelectThumbnailButton;
	JButton[] frontSelectThumbnailButton;
	JButton mipButton;
	JButton openTopThumbnailButton;
	JButton openSideThumbnailButton;
	JButton openFrontThumbnailButton;
	JButton openResizeTopButton;
	JButton openResizeSideButton;
	JButton openResizeFrontButton;
	JButton resizeTopImageButton;
	JButton resizeSideImageButton;
	JButton resizeFrontImageButton;
	JButton flipHSideButton;
	JButton flipVSideButton;
	JButton flipHTopButton;
	JButton flipVTopButton;
	JButton flipHFrontButton;
	JButton flipVFrontButton;

	JSlider topSliceSlider;
	JSlider frontSliceSlider;
	JSlider sideSliceSlider;
	JSlider topWidthSlider;
	JSlider topHeightSlider;
	JSlider sideWidthSlider;
	JSlider sideHeightSlider;
	JSlider frontWidthSlider;
	JSlider frontHeightSlider;

	BufferedImage[] topImageArray;
	BufferedImage[] sideImageArray;
	BufferedImage[] frontImageArray;
	BufferedImage topImage;
	BufferedImage frontImage;
	BufferedImage sideImage;
	BufferedImage resizeTopImage;
	BufferedImage resizeSideImage;
	BufferedImage resizeFrontImage;

	JLabel topImageIcon;
	JLabel frontImageIcon;
	JLabel sideImageIcon;
	JLabel resizeTopImageIcon;
	JLabel resizeSideImageIcon;
	JLabel resizeFrontImageIcon;
	JLabel topWSliderValue;
	JLabel topHSliderValue;
	JLabel frontWSliderValue;
	JLabel frontHSliderValue;
	JLabel sideWSliderValue;
	JLabel sideHSliderValue;

	short cthead[][][]; // store the 3D volume data set
	short min;
	short max;
	static final short MAX_PROJECTION_VALUE = -15000;

	public void Coursework() throws IOException {

		System.out.print("Enter the file-name: ");
		Scanner enterFileName = new Scanner(System.in);
		String fileName = enterFileName.next();

		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("File does not exist");
			System.exit(0);
		}

		topImage = new BufferedImage(WIDTH, LENGTH, BufferedImage.TYPE_3BYTE_BGR);
		frontImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		sideImage = new BufferedImage(LENGTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

		int i, j, k;

		min = Short.MAX_VALUE;
		max = Short.MIN_VALUE;
		short read;
		int b1, b2;

		cthead = new short[HEIGHT][WIDTH][LENGTH];

		// For loops used to correct the Endianess
		for (k = 0; k < HEIGHT; k++) {
			for (j = 0; j < WIDTH; j++) {
				for (i = 0; i < LENGTH; i++) {

					b1 = ((int) in.readByte()) & 0xff;
					b2 = ((int) in.readByte()) & 0xff;
					read = (short) ((b2 << 8) | b1);
					if (read < min) {
						min = read;
					} // Update the minimum
					if (read > max) {
						max = read;
					} // Update the maximum
					cthead[k][j][i] = read;
				}
			}
		}
		System.out.println(min + " " + max); // This should output -1117, 2248

		topImageArray = new BufferedImage[HEIGHT];
		topSelectThumbnailButton = new JButton[HEIGHT];

		sideImageArray = new BufferedImage[WIDTH];
		sideSelectThumbnailButton = new JButton[WIDTH];

		frontImageArray = new BufferedImage[LENGTH];
		frontSelectThumbnailButton = new JButton[LENGTH];

		Container container = getContentPane();
		container.setLayout(new FlowLayout());
		container.setBackground(Color.GRAY);

		// Create icons
		topImageIcon = new JLabel(new ImageIcon(topImage));
		frontImageIcon = new JLabel(new ImageIcon(frontImage));
		sideImageIcon = new JLabel(new ImageIcon(sideImage));

		// Create sliders
		topSliceSlider = new JSlider(0, HEIGHT - 1);
		frontSliceSlider = new JSlider(0, LENGTH - 1);
		sideSliceSlider = new JSlider(0, WIDTH - 1);
		topSliceSlider.setBackground(Color.GRAY);
		frontSliceSlider.setBackground(Color.GRAY);
		sideSliceSlider.setBackground(Color.GRAY);

		// Add labels
		frontSliceSlider.setMajorTickSpacing(50);
		frontSliceSlider.setMinorTickSpacing(10);
		frontSliceSlider.setPaintTicks(true);
		frontSliceSlider.setPaintLabels(true);

		// Add labels
		topSliceSlider.setMajorTickSpacing(20);
		topSliceSlider.setMinorTickSpacing(10);
		topSliceSlider.setPaintTicks(true);
		topSliceSlider.setPaintLabels(true);

		// Add labels
		sideSliceSlider.setMajorTickSpacing(50);
		sideSliceSlider.setMinorTickSpacing(10);
		sideSliceSlider.setPaintTicks(true);
		sideSliceSlider.setPaintLabels(true);

		Container topDown = new Container();
		topDown.setLayout(new GridLayout(3, 1));
		JLabel title1 = new JLabel("Top-down View");
		topDown.add(title1);
		topDown.add(topImageIcon);
		topDown.add(topSliceSlider);

		Container sideView = new Container();
		sideView.setLayout(new GridLayout(3, 1));
		JLabel title2 = new JLabel("Side View");
		sideView.add(title2);
		sideView.add(sideImageIcon);
		sideView.add(sideSliceSlider);

		Container frontView = new Container();
		frontView.setLayout(new GridLayout(3, 1));
		JLabel title3 = new JLabel("Front View");
		frontView.add(title3);
		frontView.add(frontImageIcon);
		frontView.add(frontSliceSlider);

		Container buttonsView = new Container();
		buttonsView.setLayout(new GridLayout(7, 1));

		// Creates all buttons in main window
		mipButton = new JButton("MIP");
		openTopThumbnailButton = new JButton("Thumbnail Top View");
		openSideThumbnailButton = new JButton("Thumbnail Side View");
		openFrontThumbnailButton = new JButton("Thumbnail Front View");
		openResizeTopButton = new JButton("Resize Top View");
		openResizeSideButton = new JButton("Resize Side View");
		openResizeFrontButton = new JButton("Resize Front View");

		// Adds all buttons to container in main window
		buttonsView.add(mipButton);
		buttonsView.add(openTopThumbnailButton);
		buttonsView.add(openSideThumbnailButton);
		buttonsView.add(openFrontThumbnailButton);
		buttonsView.add(openResizeTopButton);
		buttonsView.add(openResizeSideButton);
		buttonsView.add(openResizeFrontButton);

		container.add(topDown);
		container.add(sideView);
		container.add(frontView);
		container.add(buttonsView);

		GUIEventHandler handler = new GUIEventHandler();

		mipButton.addActionListener(handler);
		openTopThumbnailButton.addActionListener(handler);
		openSideThumbnailButton.addActionListener(handler);
		openFrontThumbnailButton.addActionListener(handler);
		openResizeTopButton.addActionListener(handler);
		openResizeSideButton.addActionListener(handler);
		openResizeFrontButton.addActionListener(handler);
		frontSliceSlider.addChangeListener(handler);
		topSliceSlider.addChangeListener(handler);
		sideSliceSlider.addChangeListener(handler);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private class GUIEventHandler implements ActionListener, ChangeListener {

		// Change handler for sliders
		public void stateChanged(ChangeEvent event) {

			if (event.getSource() == topSliceSlider) {
				topImage = MIP(topImage, 1, false);
				topImageIcon.setIcon(new ImageIcon(topImage));
			} else if (event.getSource() == frontSliceSlider) {
				frontImage = MIP(frontImage, 2, false);
				frontImageIcon.setIcon(new ImageIcon(frontImage));
			} else if (event.getSource() == sideSliceSlider) {
				sideImage = MIP(sideImage, 3, false);
				sideImageIcon.setIcon(new ImageIcon(sideImage));
			} else if (event.getSource() == sideWidthSlider) {
				sideWSliderValue.setText("Width: " + sideWidthSlider.getValue());
			} else if (event.getSource() == sideHeightSlider) {
				sideHSliderValue.setText("Height: " + sideHeightSlider.getValue());
			} else if (event.getSource() == topWidthSlider) {
				topWSliderValue.setText("Width: " + topWidthSlider.getValue());
			} else if (event.getSource() == topHeightSlider) {
				topHSliderValue.setText("Height: " + topHeightSlider.getValue());
			} else if (event.getSource() == frontWidthSlider) {
				frontWSliderValue.setText("Width: " + frontWidthSlider.getValue());
			} else if (event.getSource() == frontHeightSlider) {
				frontHSliderValue.setText("Height: " + frontHeightSlider.getValue());
			}
		}

		// Action handlers for buttons
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == mipButton) {
				topImage = MIP(topImage, 1, true);
				frontImage = MIP(frontImage, 2, true);
				sideImage = MIP(sideImage, 3, true);

				topImageIcon.setIcon(new ImageIcon(topImage));
				frontImageIcon.setIcon(new ImageIcon(frontImage));
				sideImageIcon.setIcon(new ImageIcon(sideImage));
			} else if (event.getSource() == openTopThumbnailButton) {
				thumbnailTopWindow();
			} else if (event.getSource() == openSideThumbnailButton) {
				thumbnailSideWindow();
			} else if (event.getSource() == openFrontThumbnailButton) {
				thumbnailFrontWindow();
			} else if (event.getSource() == openResizeTopButton) {
				resizeTopWindow();
			} else if (event.getSource() == openResizeSideButton) {
				resizeSideWindow();
			} else if (event.getSource() == openResizeFrontButton) {
				resizeFrontWindow();
			} else if (event.getSource() == resizeTopImageButton) {
				// If sliders are at 0, change them to 1
				if (topWidthSlider.getValue() == 0) {
					topWidthSlider.setValue(1);
				}
				if (topHeightSlider.getValue() == 0) {
					topHeightSlider.setValue(1);
				}

				resizeTopImage = resizeImage(topImage, topWidthSlider.getValue(), topHeightSlider.getValue());
				resizeTopImageIcon.setIcon(new ImageIcon(resizeTopImage));
			} else if (event.getSource() == resizeSideImageButton) {
				// If sliders are at 0, change them to 1
				if (sideWidthSlider.getValue() == 0) {
					sideWidthSlider.setValue(1);
				}
				if (sideHeightSlider.getValue() == 0) {
					sideHeightSlider.setValue(1);
				}

				resizeSideImage = resizeImage(sideImage, sideWidthSlider.getValue(), sideHeightSlider.getValue());
				resizeSideImageIcon.setIcon(new ImageIcon(resizeSideImage));
			} else if (event.getSource() == resizeFrontImageButton) {
				// If sliders are at 0, change them to 1
				if (frontWidthSlider.getValue() == 0) {
					frontWidthSlider.setValue(1);
				}
				if (frontHeightSlider.getValue() == 0) {
					frontHeightSlider.setValue(1);
				}

				resizeFrontImage = resizeImage(frontImage, frontWidthSlider.getValue(), frontHeightSlider.getValue());
				resizeFrontImageIcon.setIcon(new ImageIcon(resizeFrontImage));
			} else if (event.getSource() == flipHSideButton) { // Flip
																// horizontally,
																// side view
				resizeSideImage = flipHorizontally(resizeSideImage);
				resizeSideImageIcon.setIcon(new ImageIcon(resizeSideImage));
			} else if (event.getSource() == flipVSideButton) { // Flip
																// vertically,
																// side view
				resizeSideImage = flipVertically(resizeSideImage);
				resizeSideImageIcon.setIcon(new ImageIcon(resizeSideImage));
			} else if (event.getSource() == flipHTopButton) { // Flip
																// horizontally,
																// top-down view
				resizeTopImage = flipHorizontally(resizeTopImage);
				resizeTopImageIcon.setIcon(new ImageIcon(resizeTopImage));
			} else if (event.getSource() == flipVTopButton) { // Flip
																// vertically,
																// top-down view
				resizeTopImage = flipVertically(resizeTopImage);
				resizeTopImageIcon.setIcon(new ImageIcon(resizeTopImage));
			} else if (event.getSource() == flipHFrontButton) { // Flip
																// horizontally,
																// front view
				resizeFrontImage = flipHorizontally(resizeFrontImage);
				resizeFrontImageIcon.setIcon(new ImageIcon(resizeFrontImage));
			} else if (event.getSource() == flipVFrontButton) { // Flip
																// vertically,
																// front view
				resizeFrontImage = flipVertically(resizeFrontImage);
				resizeFrontImageIcon.setIcon(new ImageIcon(resizeFrontImage));
			} else {

				// These are for the buttons found in the thumbnail windows
				// Thumbnail buttons for top-down view
				int i = 0;
				while ((i < HEIGHT) && (topSelectThumbnailButton[i] != null)) {
					if (event.getSource() == topSelectThumbnailButton[i]) {
						JFrame topSelected = new JFrame(
								"Selected thumbnail " + (i + 1) + "/" + topSelectThumbnailButton.length);
						topSelected.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

						BufferedImage image = new BufferedImage(WIDTH, LENGTH, BufferedImage.TYPE_3BYTE_BGR);
						image = resizeImage(topImageArray[i], WIDTH, LENGTH);
						JLabel imageIcon = new JLabel(new ImageIcon(image));

						topSelected.add(imageIcon);
						topSelected.pack();
						topSelected.setLocationRelativeTo(null);
						topSelected.setVisible(true);
					}
					i++;
				}

				// Thumbnail buttons for side view
				int j = 0;
				while ((j < WIDTH) && (sideSelectThumbnailButton[j] != null)) {
					if (event.getSource() == sideSelectThumbnailButton[j]) {
						JFrame sideSelected = new JFrame(
								"Selected thumbnail " + (j + 1) + "/" + sideSelectThumbnailButton.length);
						sideSelected.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

						BufferedImage image = new BufferedImage(LENGTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
						image = resizeImage(sideImageArray[j], LENGTH, HEIGHT);
						JLabel imageIcon = new JLabel(new ImageIcon(image));

						sideSelected.add(imageIcon);
						sideSelected.pack();
						sideSelected.setLocationRelativeTo(null);
						sideSelected.setVisible(true);
					}
					j++;
				}

				// Thumbnail buttons for front view
				int k = 0;
				while ((k < LENGTH) && (frontSelectThumbnailButton[k] != null)) {
					if (event.getSource() == frontSelectThumbnailButton[k]) {
						JFrame frontSelected = new JFrame(
								"Selected thumbnail " + (k + 1) + "/" + frontSelectThumbnailButton.length);
						frontSelected.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

						BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
						image = resizeImage(frontImageArray[k], WIDTH, HEIGHT);
						JLabel imageIcon = new JLabel(new ImageIcon(image));

						frontSelected.add(imageIcon);
						frontSelected.pack();
						frontSelected.setLocationRelativeTo(null);
						frontSelected.setVisible(true);
					}
					k++;
				}

			}
		}

	}

	// Returns the image as an array of bytes
	public static byte[] GetImageData(BufferedImage image) {
		WritableRaster write = image.getRaster();
		DataBuffer buffer = write.getDataBuffer();
		if (buffer.getDataType() != DataBuffer.TYPE_BYTE) {
			throw new IllegalStateException("Not of type byte");
		}
		return ((DataBufferByte) buffer).getData();
	}

	// Updates the images to show the current selected slice
	public BufferedImage MIP(BufferedImage image, int stateChange, boolean mip) {

		int w = image.getWidth();
		int h = image.getHeight();

		int c;

		byte[] data = GetImageData(image);
		float col;
		short datum;

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				if (stateChange == 1) { // Top-down view
					if (mip) {
						short mipValue = maximumProjection(cthead, stateChange, i, j);
						col = (255.0f * ((float) mipValue - (float) min) / ((float) (max - min)));
					} else {
						datum = cthead[topSliceSlider.getValue()][j][i];
						col = (255.0f * ((float) datum - (float) min) / ((float) (max - min)));
					}
					for (c = 0; c < 3; c++) {
						data[c + 3 * i + 3 * j * w] = (byte) col;
					}
				} else if (stateChange == 2) { // Front view
					if (mip) {
						short mipValue = maximumProjection(cthead, stateChange, i, j);
						col = (255.0f * (((float) mipValue - (float) min) / ((float) (max - min))));
					} else {
						datum = cthead[j][frontSliceSlider.getValue()][i];
						col = (255.0f * ((float) datum - (float) min) / ((float) (max - min)));
					}
					for (c = 0; c < 3; c++) {
						data[c + 3 * i + 3 * j * w] = (byte) col;
					}
				} else if (stateChange == 3) { // Side view
					if (mip) {
						short mipValue = maximumProjection(cthead, stateChange, i, j);
						col = (255.0f * ((float) mipValue - (float) min) / ((float) (max - min)));
					} else {
						datum = cthead[j][i][sideSliceSlider.getValue()];
						col = (255.0f * ((float) datum - (float) min) / ((float) (max - min)));
					}
					for (c = 0; c < 3; c++) {
						data[c + 3 * i + 3 * j * w] = (byte) col;
					}
				}
			}
		}
		return image;
	}

	// A resize function specified for the thumbnails
	public BufferedImage resizeImageThumbnail(BufferedImage image) {
		int width = image.getWidth() / 3;
		int height = image.getHeight() / 3;

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		byte[] dataA = GetImageData(image);
		byte[] dataB = GetImageData(newImage);

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				for (int c = 0; c < 3; c++) {
					float yPos = j * image.getHeight() / height;
					float xPos = i * image.getWidth() / width;
					dataB[c + 3 * i + 3 * j * width] = dataA[(int) (c + 3 * xPos + 3 * yPos * image.getWidth())];
				}
			}
		}

		return newImage;
	}

	// A resize function for any arbitrary size
	public BufferedImage resizeImage(BufferedImage image, int width, int height) {
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		byte[] dataA = GetImageData(image);
		byte[] dataB = GetImageData(newImage);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				for (int c = 0; c < 3; c++) {
					float yPos = j * image.getHeight() / height;
					float xPos = i * image.getWidth() / width;

					dataB[c + 3 * i + 3 * j * width] = dataA[(int) (c + 3 * xPos + 3 * yPos * image.getWidth())];
				}
			}
		}
		return newImage;
	}

	// Flips the image horizontally
	public BufferedImage flipHorizontally(BufferedImage oldImage) {
		int width = oldImage.getWidth();
		int height = oldImage.getHeight();

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		byte[] dataA = GetImageData(oldImage);
		byte[] dataB = GetImageData(newImage);

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int newI = width - i - 1;
				for (int c = 0; c < 3; c++) {
					dataB[c + 3 * i + 3 * j * width] = dataA[c + 3 * newI + 3 * j * width];
				}
			}
		}
		return newImage;
	}

	// Flips the image vertically
	public BufferedImage flipVertically(BufferedImage oldImage) {

		int width = oldImage.getWidth();
		int height = oldImage.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		byte[] dataA = GetImageData(oldImage);
		byte[] dataB = GetImageData(newImage);

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int newJ = height - j - 1;
				for (int c = 0; c < 3; c++) {
					dataB[c + 3 * i + 3 * j * width] = dataA[c + 3 * i + 3 * newJ * width];
				}
			}
		}
		return newImage;
	}

	// This finds the maximum value for each pixel by looking through cthead
	public short maximumProjection(short[][][] cthead, int axis, int xPos, int yPos) {
		short maximumValue = MAX_PROJECTION_VALUE;

		if (axis == 1) { // Top-down view
			for (int w = 0; w < HEIGHT; w++) {
				if (cthead[w][yPos][xPos] > maximumValue) {
					maximumValue = cthead[w][yPos][xPos];
				}
			}
		} else if (axis == 2) { // Front view
			// This MIP has a different length because the pillow in cthead
			// interferes
			// with the image of the skull, making it harder to see clearly
			for (int w = 0; w < LENGTH - 50; w++) {
				if (cthead[yPos][w][xPos] > maximumValue) {
					maximumValue = cthead[yPos][w][xPos];
				}
			}
		} else if (axis == 3) { // Side view
			for (int w = 0; w < WIDTH; w++) {
				if (cthead[yPos][xPos][w] > maximumValue) {
					maximumValue = cthead[yPos][xPos][w];
				}
			}
		}
		return maximumValue;
	}

	// Opens the window for resizing the top image
	public void resizeTopWindow() {
		GUIEventHandler handler = new GUIEventHandler();
		JFrame resize_top_container = new JFrame("Resize top view");
		resize_top_container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		resizeTopContainer = new Container();
		resizeTopContainer.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(resizeTopContainer);

		// Creates the width slider
		topWidthSlider = new JSlider(0, 512);
		topWidthSlider.setMajorTickSpacing(100);
		topWidthSlider.setMinorTickSpacing(50);
		topWidthSlider.setPaintTicks(true);
		topWidthSlider.setPaintLabels(true);

		// Creates the height slider
		topHeightSlider = new JSlider(0, 512);
		topHeightSlider.setMajorTickSpacing(100);
		topHeightSlider.setMinorTickSpacing(50);
		topHeightSlider.setPaintTicks(true);
		topHeightSlider.setPaintLabels(true);

		resizeTopImage = topImage;
		resizeTopImageIcon = new JLabel(new ImageIcon(resizeTopImage));

		topWSliderValue = new JLabel("Width: " + topWidthSlider.getValue());
		topHSliderValue = new JLabel("Height: " + topHeightSlider.getValue());

		topHeightSlider.addChangeListener(handler);
		topWidthSlider.addChangeListener(handler);

		resizeTopImageButton = new JButton("Resize");
		resizeTopImageButton.addActionListener(handler);

		flipHTopButton = new JButton("Flip H");
		flipHTopButton.addActionListener(handler);

		flipVTopButton = new JButton("Flip V");
		flipVTopButton.addActionListener(handler);

		resizeTopImageButton = new JButton("Resize");
		resizeTopImageButton.addActionListener(handler);

		// alterBar contains the sliders, labels and buttons
		Container alterBar = new Container();
		alterBar.setLayout(new GridLayout(2, 3));

		// flipGrid is for the flipH and flipV buttons
		Container flipGrid = new Container();
		flipGrid.setLayout(new GridLayout(1, 2));

		alterBar.add(topWidthSlider);
		alterBar.add(topHeightSlider);
		alterBar.add(resizeTopImageButton);
		alterBar.add(topWSliderValue);
		alterBar.add(topHSliderValue);

		flipGrid.add(flipHTopButton);
		flipGrid.add(flipVTopButton);

		alterBar.add(flipGrid);

		resizeTopContainer.add(alterBar, BorderLayout.NORTH);
		resizeTopContainer.add(resizeTopImageIcon, BorderLayout.CENTER);

		resize_top_container.add(scroll);
		resize_top_container.setPreferredSize(new Dimension(700, 500));
		resize_top_container.pack();
		resize_top_container.setLocationRelativeTo(null);
		resize_top_container.setVisible(true);

	}

	// Opens the window for resizing the side image
	public void resizeSideWindow() {
		GUIEventHandler handler = new GUIEventHandler();
		JFrame resize_side_container = new JFrame("Resize side view");
		resize_side_container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		resizeSideContainer = new Container();
		resizeSideContainer.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(resizeSideContainer);

		sideWidthSlider = new JSlider(0, 512);
		sideWidthSlider.setMajorTickSpacing(100);
		sideWidthSlider.setMinorTickSpacing(50);
		sideWidthSlider.setPaintTicks(true);
		sideWidthSlider.setPaintLabels(true);

		sideHeightSlider = new JSlider(0, 512);
		sideHeightSlider.setMajorTickSpacing(100);
		sideHeightSlider.setMinorTickSpacing(50);
		sideHeightSlider.setPaintTicks(true);
		sideHeightSlider.setPaintLabels(true);
		sideHeightSlider.setValue(HEIGHT);

		resizeSideImage = new BufferedImage(LENGTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		resizeSideImage = sideImage;
		resizeSideImageIcon = new JLabel(new ImageIcon(resizeSideImage));

		sideWSliderValue = new JLabel("Width: " + sideWidthSlider.getValue());
		sideHSliderValue = new JLabel("Height: " + sideHeightSlider.getValue());

		sideHeightSlider.addChangeListener(handler);
		sideWidthSlider.addChangeListener(handler);

		resizeSideImageButton = new JButton("Resize");
		resizeSideImageButton.addActionListener(handler);

		flipHSideButton = new JButton("Flip H");
		flipHSideButton.addActionListener(handler);

		flipVSideButton = new JButton("Flip V");
		flipVSideButton.addActionListener(handler);

		Container alterBar = new Container();
		alterBar.setLayout(new GridLayout(2, 3));

		Container flipGrid = new Container();
		flipGrid.setLayout(new GridLayout(1, 2));

		alterBar.add(sideWidthSlider);
		alterBar.add(sideHeightSlider);
		alterBar.add(resizeSideImageButton);
		alterBar.add(sideWSliderValue);
		alterBar.add(sideHSliderValue);

		flipGrid.add(flipHSideButton);
		flipGrid.add(flipVSideButton);

		alterBar.add(flipGrid);

		resizeSideContainer.add(alterBar, BorderLayout.NORTH);
		resizeSideContainer.add(resizeSideImageIcon, BorderLayout.CENTER);

		resize_side_container.add(scroll);
		resize_side_container.setPreferredSize(new Dimension(700, 500));
		resize_side_container.pack();
		resize_side_container.setLocationRelativeTo(null);
		resize_side_container.setVisible(true);

	}

	// Opens the window for resizing the front image
	public void resizeFrontWindow() {
		GUIEventHandler handler = new GUIEventHandler();
		JFrame resize_front_container = new JFrame("Resize front view");
		resize_front_container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		resizeFrontContainer = new Container();
		resizeFrontContainer.setLayout(new BorderLayout());

		JScrollPane scroll = new JScrollPane(resizeFrontContainer);
		frontWidthSlider = new JSlider(0, 512);
		frontWidthSlider.setMajorTickSpacing(100);
		frontWidthSlider.setMinorTickSpacing(50);
		frontWidthSlider.setPaintTicks(true);
		frontWidthSlider.setPaintLabels(true);

		frontHeightSlider = new JSlider(0, 512);
		frontHeightSlider.setMajorTickSpacing(100);
		frontHeightSlider.setMinorTickSpacing(50);
		frontHeightSlider.setPaintTicks(true);
		frontHeightSlider.setPaintLabels(true);
		frontHeightSlider.setValue(HEIGHT);

		resizeFrontImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		resizeFrontImage = frontImage;
		resizeFrontImageIcon = new JLabel(new ImageIcon(resizeFrontImage));

		frontWSliderValue = new JLabel("Width: " + frontWidthSlider.getValue());
		frontHSliderValue = new JLabel("Height: " + frontHeightSlider.getValue());

		frontHeightSlider.addChangeListener(handler);
		frontWidthSlider.addChangeListener(handler);

		resizeFrontImageButton = new JButton("Resize");
		resizeFrontImageButton.addActionListener(handler);

		flipHFrontButton = new JButton("Flip H");
		flipHFrontButton.addActionListener(handler);

		flipVFrontButton = new JButton("Flip V");
		flipVFrontButton.addActionListener(handler);

		Container alterBar = new Container();
		alterBar.setLayout(new GridLayout(2, 3));

		Container flipGrid = new Container();
		flipGrid.setLayout(new GridLayout(1, 2));

		alterBar.add(frontWidthSlider);
		alterBar.add(frontHeightSlider);
		alterBar.add(resizeFrontImageButton);
		alterBar.add(frontWSliderValue);
		alterBar.add(frontHSliderValue);

		flipGrid.add(flipHFrontButton);
		flipGrid.add(flipVFrontButton);

		alterBar.add(flipGrid);

		resizeFrontContainer.add(alterBar, BorderLayout.NORTH);
		resizeFrontContainer.add(resizeFrontImageIcon, BorderLayout.CENTER);

		resize_front_container.add(scroll);
		resize_front_container.setPreferredSize(new Dimension(700, 500));
		resize_front_container.pack();
		resize_front_container.setLocationRelativeTo(null);
		resize_front_container.setVisible(true);

	}

	// Opens the window for displaying the top-down image thumbnails
	public void thumbnailTopWindow() {
		GUIEventHandler handler = new GUIEventHandler();
		JFrame thumbnailTopContainer = new JFrame("Thumbnail top view");
		thumbnailTopContainer.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		Container top_grid = new Container();
		JScrollPane scroll = new JScrollPane(top_grid);
		top_grid.setLayout(new GridLayout(0, 6, 5, 5));
		JLabel[] imageIconArray = new JLabel[HEIGHT];

		// Save the position of the slider
		int lastPos = topSliceSlider.getValue();

		for (int i = 0; i < HEIGHT; i++) {
			topSliceSlider.setValue(i);

			// Add the thumbnail
			topImageArray[i] = new BufferedImage(WIDTH, LENGTH, BufferedImage.TYPE_3BYTE_BGR);
			topImageArray[i] = MIP(topImageArray[i], 1, false);
			topImageArray[i] = resizeImageThumbnail(topImageArray[i]);
			imageIconArray[i] = new JLabel(new ImageIcon(topImageArray[i]));
			top_grid.add(imageIconArray[i]);

			// Add the label
			JLabel label = new JLabel("Slice " + (i + 1) + " / " + HEIGHT);
			top_grid.add(label);

			// Add the button
			topSelectThumbnailButton[i] = new JButton("Select");
			topSelectThumbnailButton[i].addActionListener(handler);
			top_grid.add(topSelectThumbnailButton[i]);
		}
		// Restore the position of the slider
		topSliceSlider.setValue(lastPos);

		thumbnailTopContainer.setSize(700, 500);
		thumbnailTopContainer.add(scroll);
		thumbnailTopContainer.setLocationRelativeTo(null);
		thumbnailTopContainer.setVisible(true);

	}

	// Opens the window for displaying the side image thumbnails
	public void thumbnailSideWindow() {
		JFrame thumbnailSideContainer = new JFrame("Thumbnail side view");
		GUIEventHandler handler = new GUIEventHandler();
		thumbnailSideContainer.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		Container side_grid = new Container();
		JScrollPane scroll = new JScrollPane(side_grid);
		side_grid.setLayout(new GridLayout(0, 9, 5, 5));
		JLabel[] imageIconArray = new JLabel[WIDTH];

		// Save the position of the slider
		int lastPos = sideSliceSlider.getValue();

		for (int i = 0; i < WIDTH; i++) {
			sideSliceSlider.setValue(i);

			// Add the thumbnail
			sideImageArray[i] = new BufferedImage(LENGTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
			sideImageArray[i] = MIP(sideImageArray[i], 3, false);
			sideImageArray[i] = resizeImageThumbnail(sideImageArray[i]);
			imageIconArray[i] = new JLabel(new ImageIcon(sideImageArray[i]));
			side_grid.add(imageIconArray[i]);

			// Add the label
			JLabel label = new JLabel("Slice " + (i + 1) + " / " + WIDTH);
			side_grid.add(label);

			// Add the button
			sideSelectThumbnailButton[i] = new JButton("Select");
			sideSelectThumbnailButton[i].addActionListener(handler);
			side_grid.add(sideSelectThumbnailButton[i]);
		}
		sideSliceSlider.setValue(lastPos);

		thumbnailSideContainer.setSize(840, 500);
		thumbnailSideContainer.add(scroll);
		thumbnailSideContainer.setLocationRelativeTo(null);
		thumbnailSideContainer.setVisible(true);
	}

	// Opens the window for displaying the front image thumbnails
	public void thumbnailFrontWindow() {
		GUIEventHandler handler = new GUIEventHandler();
		JFrame thumbnailFrontContainer = new JFrame("Thumbnail front view");
		thumbnailFrontContainer.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		Container front_grid = new Container();
		JScrollPane scroll = new JScrollPane(front_grid);
		front_grid.setLayout(new GridLayout(0, 9, 5, 5));
		JLabel[] imageIconArray = new JLabel[LENGTH];

		int lastPos = frontSliceSlider.getValue();

		for (int i = 0; i < LENGTH; i++) {
			frontSliceSlider.setValue(i);

			// Add the thumbnail
			frontImageArray[i] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
			frontImageArray[i] = MIP(frontImageArray[i], 2, false);
			frontImageArray[i] = resizeImageThumbnail(frontImageArray[i]);
			imageIconArray[i] = new JLabel(new ImageIcon(frontImageArray[i]));
			front_grid.add(imageIconArray[i]);

			// Add the label
			JLabel label = new JLabel("Slice " + (i + 1) + " / " + LENGTH);
			front_grid.add(label);

			// Add the button
			frontSelectThumbnailButton[i] = new JButton("Select");
			frontSelectThumbnailButton[i].addActionListener(handler);
			front_grid.add(frontSelectThumbnailButton[i]);
		}
		// Restore the position of the slider
		frontSliceSlider.setValue(lastPos);

		thumbnailFrontContainer.setSize(840, 500);
		thumbnailFrontContainer.add(scroll);
		thumbnailFrontContainer.setLocationRelativeTo(null);
		thumbnailFrontContainer.setVisible(true);
	}

	// Main method to run the program
	public static void main(String[] args) throws IOException {
		Coursework window = new Coursework();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Coursework");
		window.Coursework();
	}
}