package fiji.plugin.trackmate.visualization.trackscheme;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxGdCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;

import fiji.plugin.trackmate.util.DefaultFileFilter;

/**
 * $Id: EditorActions.java,v 1.35 2011-02-14 15:45:58 gaudenz Exp $
 * Copyright (c) 2001-2010, Gaudenz Alder, David Benson
 * 
 * All rights reserved.
 * 
 * See LICENSE file for license details. If you are unable to locate
 * this file please contact info (at) jgraph (dot) com.
 */
public class SaveAction extends AbstractAction {

	private static final long serialVersionUID = 7672151690754466760L;
	private static final ImageIcon ICON = new ImageIcon(TrackSchemeFrame.class.getResource("resources/camera_export.png"));
	protected String lastDir = null;
	protected TrackSchemeFrame frame;

	/**
	 * 
	 */
	public SaveAction(TrackSchemeFrame frame) {
		putValue(Action.SMALL_ICON, ICON);
		this.frame = frame;
		
	}

	/**
	 * Saves XML+PNG format.
	 */
	protected void saveXmlPng(TrackSchemeFrame frame, String filename, Color bg) throws IOException {
		mxGraphComponent graphComponent = frame.graphComponent;
		mxGraph graph = frame.graph;

		// Creates the image for the PNG file
		BufferedImage image = mxCellRenderer.createBufferedImage(graph,	null, 1, bg, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());

		// Creates the URL-encoded XML data
		mxCodec codec = new mxCodec();
		String xml = URLEncoder.encode(mxUtils.getXml(codec.encode(graph.getModel())), "UTF-8");
		mxPngEncodeParam param = mxPngEncodeParam.getDefaultEncodeParam(image);
		param.setCompressedText(new String[] { "mxGraphModel", xml });

		// Saves as a PNG file
		FileOutputStream outputStream = new FileOutputStream(new File(filename));
		try {
			mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream,	param);

			if (image != null) {
				encoder.encode(image);
			} else {
				JOptionPane.showMessageDialog(graphComponent, "No Image Data");
			}
		} finally {
			outputStream.close();
		}
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		mxGraphComponent graphComponent = frame.graphComponent;
		mxGraph graph =frame.graph;
		FileFilter selectedFilter = null;
		DefaultFileFilter xmlPngFilter = new DefaultFileFilter(".png", "PNG+XML file (.png)");
		FileFilter vmlFileFilter = new DefaultFileFilter(".html", "VML file (.html)");
		String filename = null;
		boolean dialogShown = false;

		String wd;

		if (lastDir != null) {
			wd = lastDir;
		} else {
			wd = System.getProperty("user.dir");
		}

		JFileChooser fc = new JFileChooser(wd);

		// Adds the default file format
		FileFilter defaultFilter = xmlPngFilter;
		fc.addChoosableFileFilter(defaultFilter);

		// Adds special vector graphics formats and HTML
		fc.addChoosableFileFilter(new DefaultFileFilter(".mxe", "mxGraph Editor file (.mxe)"));
		fc.addChoosableFileFilter(new DefaultFileFilter(".txt", "Graph Drawing file (.txt)"));
		fc.addChoosableFileFilter(new DefaultFileFilter(".svg", "SVG file (.svg)"));
		fc.addChoosableFileFilter(vmlFileFilter);
		fc.addChoosableFileFilter(new DefaultFileFilter(".html", "HTML file (.html)"));

		// Adds a filter for each supported image format
		Object[] imageFormats = ImageIO.getReaderFormatNames();

		// Finds all distinct extensions
		HashSet<String> formats = new HashSet<String>();

		for (int i = 0; i < imageFormats.length; i++) {
			String ext = imageFormats[i].toString().toLowerCase();
			formats.add(ext);
		}

		imageFormats = formats.toArray();

		for (int i = 0; i < imageFormats.length; i++) {
			String ext = imageFormats[i].toString();
			fc.addChoosableFileFilter(new DefaultFileFilter("."	+ ext, ext.toUpperCase() + " File  (." + ext + ")"));
		}

		// Adds filter that accepts all supported image formats
		fc.addChoosableFileFilter(new DefaultFileFilter.ImageFileFilter("All Images"));
		fc.setFileFilter(defaultFilter);
		int rc = fc.showDialog(null,"Save");
		dialogShown = true;

		if (rc != JFileChooser.APPROVE_OPTION) {
			return;
		} else {
			lastDir = fc.getSelectedFile().getParent();
		}

		filename = fc.getSelectedFile().getAbsolutePath();
		selectedFilter = fc.getFileFilter();

		if (selectedFilter instanceof DefaultFileFilter) {
			String ext = ((DefaultFileFilter) selectedFilter).getExtension();

			if (!filename.toLowerCase().endsWith(ext)) {
				filename += ext;
			}
		}

		if (new File(filename).exists() && JOptionPane.showConfirmDialog(graphComponent, "Overwrite existing file?") != JOptionPane.YES_OPTION) {
			return;
		}


		try {
			String ext = filename.substring(filename.lastIndexOf('.') + 1);

			if (ext.equalsIgnoreCase("svg")) {
				mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(graph, null, 1, null, new CanvasFactory() {
					public mxICanvas createCanvas(int width, int height) {
						mxSvgCanvas canvas = new mxSvgCanvas(mxUtils.createSvgDocument(width, height));
						canvas.setEmbedded(true);
						return canvas;
					}
				});

				mxUtils.writeFile(mxUtils.getXml(canvas.getDocument()), filename);
			
			} else if (selectedFilter == vmlFileFilter) {
				mxUtils.writeFile(mxUtils.getXml(mxCellRenderer.createVmlDocument(graph, null, 1, null, null).getDocumentElement()), filename);
			
			} else if (ext.equalsIgnoreCase("html")) {
				mxUtils.writeFile(mxUtils.getXml(mxCellRenderer.createHtmlDocument(graph, null, 1, null, null).getDocumentElement()), filename);
			
			} else if (ext.equalsIgnoreCase("mxe") || ext.equalsIgnoreCase("xml")) {
				mxCodec codec = new mxCodec();
				String xml = mxUtils.getXml(codec.encode(graph.getModel()));
				mxUtils.writeFile(xml, filename);

			} else if (ext.equalsIgnoreCase("txt")) {
				String content = mxGdCodec.encode(graph).getDocumentString();
				mxUtils.writeFile(content, filename);
			
			} else {
				Color bg = null;

				if ((!ext.equalsIgnoreCase("gif") && !ext.equalsIgnoreCase("png"))
						|| JOptionPane.showConfirmDialog(graphComponent,"Transparent Background?") != JOptionPane.YES_OPTION) {
					bg = graphComponent.getBackground();
				}

				if (selectedFilter == xmlPngFilter || (ext.equalsIgnoreCase("png") && !dialogShown)) {
					saveXmlPng(frame, filename, bg);
				} else {
					BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, bg, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());

					if (image != null) {
						ImageIO.write(image, ext, new File(filename));
					} else {
						JOptionPane.showMessageDialog(graphComponent, "No Image Data");
					}
				}
			}
		
		} catch (Throwable ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(graphComponent, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}