/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2010 - 2022 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji.plugin.trackmate.action;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CaptureOverlayPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private int firstFrame;

	private int lastFrame;

	private boolean hideImage;

	public CaptureOverlayPanel( final int firstFrame, final int lastFrame, final boolean hideImage )
	{
		this.firstFrame = firstFrame;
		this.lastFrame = lastFrame;
		this.hideImage = hideImage;

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout( gridBagLayout );

		final JLabel lblFirstFrame = new JLabel( "First frame:" );
		final GridBagConstraints gbc_lblFirstFrame = new GridBagConstraints();
		gbc_lblFirstFrame.anchor = GridBagConstraints.EAST;
		gbc_lblFirstFrame.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblFirstFrame.gridx = 0;
		gbc_lblFirstFrame.gridy = 0;
		add( lblFirstFrame, gbc_lblFirstFrame );

		final JFormattedTextField tftFirst = new JFormattedTextField( NumberFormat.getIntegerInstance() );
		tftFirst.setValue( Integer.valueOf( firstFrame ) );
		tftFirst.setColumns( 5 );
		final GridBagConstraints gbc_tftFirst = new GridBagConstraints();
		gbc_tftFirst.anchor = GridBagConstraints.NORTH;
		gbc_tftFirst.insets = new Insets( 0, 0, 5, 0 );
		gbc_tftFirst.fill = GridBagConstraints.HORIZONTAL;
		gbc_tftFirst.gridx = 1;
		gbc_tftFirst.gridy = 0;
		add( tftFirst, gbc_tftFirst );

		final JLabel lblLastFrame = new JLabel( "Last frame:" );
		final GridBagConstraints gbc_lblLastFrame = new GridBagConstraints();
		gbc_lblLastFrame.anchor = GridBagConstraints.EAST;
		gbc_lblLastFrame.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblLastFrame.gridx = 0;
		gbc_lblLastFrame.gridy = 1;
		add( lblLastFrame, gbc_lblLastFrame );

		final JFormattedTextField tftLast = new JFormattedTextField( NumberFormat.getIntegerInstance() );
		tftLast.setValue( Integer.valueOf( lastFrame ) );
		tftLast.setColumns( 5 );
		final GridBagConstraints gbc_tftLast = new GridBagConstraints();
		gbc_tftLast.insets = new Insets( 0, 0, 5, 0 );
		gbc_tftLast.fill = GridBagConstraints.HORIZONTAL;
		gbc_tftLast.gridx = 1;
		gbc_tftLast.gridy = 1;
		add( tftLast, gbc_tftLast );

		final JLabel lblNewLabel = new JLabel( "Hide image:" );
		final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		add( lblNewLabel, gbc_lblNewLabel );

		final JCheckBox chckbxHideImage = new JCheckBox();
		chckbxHideImage.setSelected( hideImage );
		final GridBagConstraints gbc_chckbxHideImage = new GridBagConstraints();
		gbc_chckbxHideImage.anchor = GridBagConstraints.WEST;
		gbc_chckbxHideImage.insets = new Insets( 0, 0, 5, 0 );
		gbc_chckbxHideImage.gridx = 1;
		gbc_chckbxHideImage.gridy = 2;
		add( chckbxHideImage, gbc_chckbxHideImage );

		final FocusListener fl = new FocusAdapter()
		{
			@Override
			public void focusGained( final FocusEvent e )
			{
				SwingUtilities.invokeLater( new Runnable()
				{
					@Override
					public void run()
					{
						( ( JFormattedTextField ) e.getSource() ).selectAll();
					}
				} );
			}
		};
		tftFirst.addFocusListener( fl );
		tftLast.addFocusListener( fl );

		tftFirst.addPropertyChangeListener( "value", ( e ) -> this.firstFrame = ( ( Number ) tftFirst.getValue() ).intValue() );
		tftLast.addPropertyChangeListener( "value", ( e ) -> this.lastFrame = ( ( Number ) tftLast.getValue() ).intValue() );
		chckbxHideImage.addActionListener( e -> this.hideImage = chckbxHideImage.isSelected() );
	}

	public int getFirstFrame()
	{
		return firstFrame;
	}

	public int getLastFrame()
	{
		return lastFrame;
	}

	public boolean isHideImage()
	{
		return hideImage;
	}

}
