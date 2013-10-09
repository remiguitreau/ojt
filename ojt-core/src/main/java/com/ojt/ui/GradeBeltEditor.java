package com.ojt.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.ojt.GradeBelt;

public class GradeBeltEditor {
	
	private static final Color BACKGROUND_COLOR = new Color(97,78,78,210);

	public static void editGradeBelt(final JTable table, final int row, final int col, final Point point) {
		try {
			final JFrame frame = (JFrame) SwingUtilities.windowForComponent(table);
			frame.setGlassPane(buildEditGradePanel(table, row, col, point));
			frame.getGlassPane().setVisible(true);
		}catch(final Exception ex) {
			
		}
	}

	private static Component buildEditGradePanel(final JTable table, final int row, final int col, final Point point) throws AWTException {
		final JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		int i=GradeBelt.values().length-1;
		double angle=2*Math.PI/(GradeBelt.values().length);
		final int rayon=120;
		final JPanel roundPanel = new JPanel();
		roundPanel.setLayout(null);
		roundPanel.setOpaque(true);
		roundPanel.setBackground(BACKGROUND_COLOR);
		roundPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					table.setValueAt(null, row, col);
				}
				GradeBeltEditor.hideGlassPaneForTable(table);
			}
		});
		final Point center = computeCenterFromPoint(point, (JFrame) SwingUtilities.windowForComponent(table));
		for(GradeBelt grade : GradeBelt.values()) {
			final GradeBelt currentGrade = grade;
			final JLabel gradeLabel = new JLabel(GradeBeltLabel.retrieveBigImageFromGrade(grade));
			gradeLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						table.setValueAt(currentGrade, row, col);
					}
					GradeBeltEditor.hideGlassPaneForTable(table);
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					gradeLabel.setBackground(new Color(255,255,255,210));
					gradeLabel.setOpaque(true);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					gradeLabel.setBackground(null);
					gradeLabel.setOpaque(false);
				}
			});
			panel.add(gradeLabel);
			gradeLabel.setBounds((int)(center.x+rayon*Math.cos(i*angle-Math.PI/2)), (int)(center.y+rayon*Math.sin(i*angle-Math.PI/2)), 84, 33);
			i--;
		}
		panel.add(roundPanel);
		roundPanel.setBounds(center.x-140, center.y-140, 360, 300);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GradeBeltEditor.hideGlassPaneForTable(table);
			}
		});
		return panel;
	}
	
	private static Point computeCenterFromPoint(Point point, JFrame frame) {
		return new Point(
				point.x<140 ? 140 : ((point.x+230)>frame.getWidth() ? (point.x-(point.x+230-frame.getWidth())) : point.x),
				point.y<140 ? 140 : ((point.y+195)>frame.getHeight() ? (point.y-(point.y+195-frame.getHeight())) : point.y));
	}

	private static void hideGlassPaneForTable(final JTable table) {
		((JFrame) SwingUtilities.windowForComponent(table)).getGlassPane().setVisible(false);
	}
	
	public static void main(String[] args) {
		final JFrame frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		final JTable table = new JTable();
		frame.getRootPane().add(table);
		frame.getRootPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GradeBeltEditor.editGradeBelt(table, 0, 0, e.getPoint());
			}
		});
		frame.setVisible(true);
	}
}
