package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;
//import javax.swing.text.StyleConstants;

import org.fife.ui.rsyntaxtextarea.ErrorStrip;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;


/**
 * The root pane used by the demos.  This allows both the applet and the
 * stand-alone application to share the same UI. 
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DemoRootPane extends JRootPane implements HyperlinkListener,
											SyntaxConstants {

	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;


	public DemoRootPane() {
		textArea = createTextArea();
		setText("JavaExample.txt");
		textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);

		scrollPane = new RTextScrollPane(textArea, true);
		Gutter gutter = scrollPane.getGutter();
		gutter.setBookmarkingEnabled(true);
		URL url = getClass().getResource("bookmark.png");
		gutter.setBookmarkIcon(new ImageIcon(url));
		getContentPane().add(scrollPane);
		ErrorStrip errorStrip = new ErrorStrip(textArea);
//errorStrip.setBackground(java.awt.Color.blue);
		getContentPane().add(errorStrip, BorderLayout.LINE_END);
		setJMenuBar(createMenuBar());
	}


	private void addSyntaxItem(String name, String res, String style,
			ButtonGroup bg, JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
				new ChangeSyntaxStyleAction(name, res, style));
		bg.add(item);
		menu.add(item);
	}


	private void addThemeItem(String name, String themeXml, ButtonGroup bg,
			JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
				new ThemeAction(name, themeXml));
		bg.add(item);
		menu.add(item);
	}


	private JMenuBar createMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu menu = new JMenu("Language");
		ButtonGroup bg = new ButtonGroup();
		addSyntaxItem("C",    "CExample.txt", SYNTAX_STYLE_C, bg, menu);
		addSyntaxItem("Java", "JavaExample.txt", SYNTAX_STYLE_JAVA, bg, menu);
		addSyntaxItem("JSON", "JsonExample.txt", SYNTAX_STYLE_JSON, bg, menu);
		addSyntaxItem("Perl", "PerlExample.txt", SYNTAX_STYLE_PERL, bg, menu);
		addSyntaxItem("PHP",  "PhpExample.txt", SYNTAX_STYLE_PHP, bg, menu);
		addSyntaxItem("Ruby", "RubyExample.txt", SYNTAX_STYLE_RUBY, bg, menu);
		addSyntaxItem("SQL",  "SQLExample.txt", SYNTAX_STYLE_SQL, bg, menu);
		addSyntaxItem("CSS",  "XMLExample.txt", SYNTAX_STYLE_CSS, bg, menu);
		addSyntaxItem("Less", "XMLExample.txt", SYNTAX_STYLE_LESS, bg, menu);
		menu.getItem(1).setSelected(true);
		mb.add(menu);

		menu = new JMenu("View");
		JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(new CodeFoldingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineHighlightAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineNumbersAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new AnimateBracketMatchingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new BookmarksAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new WordWrapAction());
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ToggleAntiAliasingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new MarkOccurrencesAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new TabLinesAction());
		menu.add(cbItem);
		mb.add(menu);

		bg = new ButtonGroup();
		menu = new JMenu("Themes");
		addThemeItem("Default", "default.xml", bg, menu);
		addThemeItem("Default (System Selection)", "default-alt.xml", bg, menu);
		addThemeItem("Dark", "dark.xml", bg, menu);
		addThemeItem("Eclipse", "eclipse.xml", bg, menu);
		addThemeItem("IDEA", "idea.xml", bg, menu);
		addThemeItem("Visual Studio", "vs.xml", bg, menu);
		mb.add(menu);

		menu = new JMenu("Help");
		JMenuItem item = new JMenuItem(new AboutAction());
		menu.add(item);
		mb.add(menu);

		return mb;

	}


	/**
	 * Creates the text area for this application.
	 *
	 * @return The text area.
	 */
	private RSyntaxTextArea createTextArea() {
		final RSyntaxTextArea textArea = new RSyntaxTextArea(25, 70);
		textArea.setTabSize(3);
		textArea.setCaretPosition(0);
		textArea.addHyperlinkListener(this);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setClearWhitespaceLinesEnabled(false);
// The stuff below is just from debugging random issues that have come up in the forums.
// TODO: Remove this stuff
//System.out.println(textArea.getMargin());
//textArea.setMargin(new java.awt.Insets(1, 15, 1, 5));
InputMap im = textArea.getInputMap();
ActionMap am = textArea.getActionMap();
im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "decreaseFontSize");
am.put("decreaseFontSize", new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction());
im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "increaseFontSize");
am.put("increaseFontSize", new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction());
//((org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaHighlighter)textArea.getHighlighter()).setDrawsLayeredHighlights(false);
//textArea.setBackground(new java.awt.Color(0xff,0x80,0x80, 0x30));
		try {
//textArea.setBackgroundImage(javax.imageio.ImageIO.read(new java.io.File("D:/temp/homer.jpg")));
		} catch (Exception e) { e.printStackTrace(); }
		
//im.put(KeyStroke.getKeyStroke('x'), "x");
//am.put("x", new AbstractAction() {
//	public void actionPerformed(ActionEvent e) {
//		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
//		javax.swing.text.Style style = doc.getStyle("comment");
//		java.awt.Color fg = Math.random() > 0.5 ? java.awt.Color.blue : java.awt.Color.red;
//		StyleConstants.setForeground(style, fg);
//	}
//});
//textArea.setSelectedTextColor(java.awt.Color.white);
//textArea.setUseSelectedTextColor(true);
		return textArea;
	}


	/**
	 * Focuses the text area.
	 */
	void focusTextArea() {
		textArea.requestFocusInWindow();
	}


	/**
	 * Called when a hyperlink is clicked in the text area.
	 *
	 * @param e The event.
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
			URL url = e.getURL();
			if (url==null) {
				UIManager.getLookAndFeel().provideErrorFeedback(null);
			}
			else {
				JOptionPane.showMessageDialog(this,
									"URL clicked:\n" + url.toString());
			}
		}
	}


	/**
	 * Sets the content in the text area to that in the specified resource.
	 *
	 * @param resource The resource to load.
	 */
	private void setText(String resource) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(resource), "UTF-8"));
			textArea.read(r, null);
			r.close();
			textArea.setCaretPosition(0);
			textArea.discardAllEdits();
		} catch (RuntimeException re) {
			throw re; // FindBugs
		} catch (Exception e) { // Never happens
			textArea.setText("Type here to see syntax highlighting");
		}
	}


	private class AboutAction extends AbstractAction {

		public AboutAction() {
			putValue(NAME, "About RSyntaxTextArea...");
		}

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(DemoRootPane.this,
					"<html><b>RSyntaxTextArea</b> - A Swing syntax highlighting text component" +
					"<br>Version 2.5.8" +
					"<br>Licensed under a modified BSD license",
					"About RSyntaxTextArea",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}


	private class AnimateBracketMatchingAction extends AbstractAction {

		public AnimateBracketMatchingAction() {
			putValue(NAME, "Animate Bracket Matching");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setAnimateBracketMatching(
						!textArea.getAnimateBracketMatching());
		}

	}


	private class BookmarksAction extends AbstractAction {

		public BookmarksAction() {
			putValue(NAME, "Bookmarks");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setIconRowHeaderEnabled(
							!scrollPane.isIconRowHeaderEnabled());
		}

	}


	private class ChangeSyntaxStyleAction extends AbstractAction {

		private String res;
		private String style;

		public ChangeSyntaxStyleAction(String name, String res, String style) {
			putValue(NAME, name);
			this.res = res;
			this.style = style;
		}

		public void actionPerformed(ActionEvent e) {
			setText(res);
			textArea.setCaretPosition(0);
			textArea.setSyntaxEditingStyle(style);
		}

	}


	private class CodeFoldingAction extends AbstractAction {

		public CodeFoldingAction() {
			putValue(NAME, "Code Folding");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setCodeFoldingEnabled(!textArea.isCodeFoldingEnabled());
		}

	}


	private class MarkOccurrencesAction extends AbstractAction {

		public MarkOccurrencesAction() {
			putValue(NAME, "Mark Occurrences");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setMarkOccurrences(!textArea.getMarkOccurrences());
		}

	}


	private class TabLinesAction extends AbstractAction {

		private boolean selected;

		public TabLinesAction() {
			putValue(NAME, "Tab Lines");
		}

		public void actionPerformed(ActionEvent e) {
			selected = !selected;
			textArea.setPaintTabLines(selected);
		}

	}


	private class ThemeAction extends AbstractAction {

		private String xml;

		public ThemeAction(String name, String xml) {
			putValue(NAME, name);
			this.xml = xml;
		}

		public void actionPerformed(ActionEvent e) {
			InputStream in = getClass().
				getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/" + xml);
			try {
				Theme theme = Theme.load(in);
				theme.apply(textArea);
//	try {
//		textArea.setBackgroundImage(javax.imageio.ImageIO.read(new java.io.File("D:/temp/homer.jpg")));
//				} catch (Exception ex) { ex.printStackTrace(); }
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}


	private class ToggleAntiAliasingAction extends AbstractAction {

		public ToggleAntiAliasingAction() {
			putValue(NAME, "Anti-Aliasing");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setAntiAliasingEnabled(!textArea.getAntiAliasingEnabled());
		}

	}


	private class ViewLineHighlightAction extends AbstractAction {

		public ViewLineHighlightAction() {
			putValue(NAME, "Current Line Highlight");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setHighlightCurrentLine(
					!textArea.getHighlightCurrentLine());
		}

	}


	private class ViewLineNumbersAction extends AbstractAction {

		public ViewLineNumbersAction() {
			putValue(NAME, "Line Numbers");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setLineNumbersEnabled(
					!scrollPane.getLineNumbersEnabled());
		}

	}


	private class WordWrapAction extends AbstractAction {

		public WordWrapAction() {
			putValue(NAME, "Word Wrap");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setLineWrap(!textArea.getLineWrap());
		}

	}


}