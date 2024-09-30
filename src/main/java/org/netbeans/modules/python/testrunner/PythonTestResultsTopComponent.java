package org.netbeans.modules.python.testrunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
//@ConvertAsProperties(dtd = "-//org.netbeans.modules.python.testrunner//PythonTestResults//EN", autostore = false)
//@TopComponent.Description(preferredID = "PythonTestResultsTopComponent", iconBase = "org/netbeans/modules/python/pytest.png", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
//@TopComponent.Registration(mode = "output", openAtStartup = false)
//@ActionID(category = "Window", id = "org.netbeans.modules.python.testrunner.PythonTestResultsTopComponent")
//@ActionReference(path = "Menu/Window/Tools", position = 255)
//@TopComponent.OpenActionRegistration(displayName = "#CTL_PythonTestResultsAction", preferredID = "PythonTestResultsTopComponent")
@Messages({
    "CTL_PythonTestResultsAction=PyTest Results",
    "CTL_PythonTestResultsTopComponent=PyTest Results Window",
    "HINT_PythonTestResultsTopComponent=This is a PyTest Results window"
})
public final class PythonTestResultsTopComponent extends TopComponent {

    private static final long serialVersionUID = 1L;

    public PythonTestResultsTopComponent() {
        initComponents();
        setName(Bundle.CTL_PythonTestResultsTopComponent());
        setToolTipText(Bundle.HINT_PythonTestResultsTopComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testResultPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        testResultPanel.setLayout(new java.awt.BorderLayout());
        add(testResultPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel testResultPanel;

    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        SwingUtilities.invokeLater(() -> {
            JFXPanel jfxPanel = new JFXPanel();
            Platform.runLater(() -> {
                URL toURL = null;
                JTextComponent lastFocusedComponent = EditorRegistry
                        .lastFocusedComponent();
                if (lastFocusedComponent != null) {
                    FileObject fileObject = NbEditorUtilities
                            .getFileObject(lastFocusedComponent.getDocument());
                    Project owner = FileOwnerQuery.getOwner(fileObject);
                    if (owner != null) {
                        try {
                            List<File> collect = Files.walk(FileUtil.toFile(owner
                                    .getProjectDirectory()).toPath())
                                    .map(Path::toFile)
                                    .filter(file -> file.getName()
                                    .equals("report.html"))
                                    .collect(Collectors.toList());
                            collect.sort(LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                            if (!collect.isEmpty()) {
                                File get = collect.get(0);
                                if (FileUtil.toFileObject(get).asText()
                                        .contains("pytest-html")) {
                                    toURL = Utilities.toURI(get).toURL();
                                }
                            }
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }

                    }

                }
                WebView webView = new WebView();
                webView.getEngine().load(toURL != null ? toURL.toString() : "");
                Scene scene = new Scene(webView);
                jfxPanel.setScene(scene);
            });
            testResultPanel.removeAll();
            testResultPanel.add(jfxPanel);
        });
    }

    @Override
    public void componentClosed() {
        testResultPanel.removeAll();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // read your settings according to their version
    }
}
