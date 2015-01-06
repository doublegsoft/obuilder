/*
 * Copyright 2015 ganguo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.doublegsoft.obuilder.netbeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.doublegsoft.obuilder.manifest.BundleInfo;
import net.doublegsoft.obuilder.manifest.BundleRequirement;
import net.doublegsoft.obuilder.manifest.ManifestParser;
import net.doublegsoft.obuilder.manifest.VersionRange;
import net.doublegsoft.obuilder.search.MavenDependency;
import net.doublegsoft.obuilder.search.MavenSearcher;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;
import org.openide.windows.TopComponent;

/**
 *
 * @author ganguo
 */
@TopComponent.Description(preferredID = "OBuilderTopComponent", iconBase = "images/obuilder-16.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.OpenActionRegistration(displayName = "OBuilder", preferredID = "OBuilderTopComponent")
public class OBuilderTopComponent extends TopComponent {

    private static final String IO_NAME = "OBuilder";

    private static final StateRenderer STATE_RENDERER = new StateRenderer();

    private final MavenSearcher searcher = new MavenSearcher();

    private JTextField outputDir;

    private JButton dirChoose;

    private JTextField jarFile;

    private JButton jarChoose;

    private JButton build;

    private JTable artifacts;

    private static JPanel flowPane(JComponent... components) {
        JPanel retVal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JComponent c : components) {
            retVal.add(c);
        }
        return retVal;
    }

    public OBuilderTopComponent() {
        setToolTipText("OBuilder");
        setDisplayName("OBuilder");
        initialize();
    }

    private void initialize() {
        JPanel content = new JPanel(new BorderLayout());
        JPanel top = new JPanel();

        top.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        //gbc.weighty = 1.0;                 // allows vertical dispersion   
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        top.add(new JLabel("Bundle Jar: "), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        top.add(flowPane(getJarFileTextField(), getJarChooseButton()), gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        top.add(new JLabel("Output Directory: "), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        top.add(flowPane(getOutputDirTextField(), getDirChooseButton()), gbc);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JScrollPane(getArtifactsTable()), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(getBuildButton());

        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        IOProvider.getDefault().getIO(IO_NAME, false).select();
    }

    private JTextField getOutputDirTextField() {
        if (outputDir == null) {
            outputDir = new JTextField();
            outputDir.setEditable(false);
            outputDir.setPreferredSize(new Dimension(450, 28));
        }
        return outputDir;
    }

    private JTextField getJarFileTextField() {
        if (jarFile == null) {
            jarFile = new JTextField();
            jarFile.setEditable(false);
            jarFile.setPreferredSize(new Dimension(450, 28));
        }
        return jarFile;
    }

    private JButton getDirChooseButton() {
        if (dirChoose == null) {
            dirChoose = new JButton();
            dirChoose.setAction(new ChooseAction(getOutputDirTextField(), new DirFilter()));
            dirChoose.setText("...");
        }
        return dirChoose;
    }

    private JButton getJarChooseButton() {
        if (jarChoose == null) {
            jarChoose = new JButton("...");
            jarChoose.setAction(new ChooseAction(getJarFileTextField(), new JarFilter()));
            jarChoose.setText("...");
        }
        return jarChoose;
    }

    private JButton getBuildButton() {
        if (build == null) {
            build = new JButton();
            build.setText("Build");
        }
        return build;
    }

    private JTable getArtifactsTable() {
        if (artifacts == null) {
            artifacts = new JTable() {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public TableCellRenderer getCellRenderer(int row, int column) {
                    if (column == 0) {
                        return STATE_RENDERER;
                    }
                    return super.getCellRenderer(row, column);
                }
            };
            artifacts.setCellSelectionEnabled(true);
            artifacts.setRowHeight(22);
            artifacts.getTableHeader().setReorderingAllowed(false);
            DefaultTableModel model = (DefaultTableModel) artifacts.getModel();
            model.addColumn("State");
            model.addColumn("Import Package");
            model.addColumn("Version Range");
            model.addColumn("Group");
            model.addColumn("Artifact");
            model.addColumn("Version");
        }
        return artifacts;
    }

    private DefaultTableModel getArtifactsModel() {
        return (DefaultTableModel) getArtifactsTable().getModel();
    }

    private void info(String message) {
        InputOutput io = IOProvider.getDefault().getIO(IO_NAME, false);
        io.select();
        try (OutputWriter out = io.getOut()) {
            out.println(message);
            out.flush();
        }
    }

    private void error(Throwable cause) {
        InputOutput io = IOProvider.getDefault().getIO(IO_NAME, false);
        try (OutputWriter out = io.getErr()) {
            cause.printStackTrace(new PrintWriter(out));
            out.flush();
        }
    }
    
    private void error(String message) {
        InputOutput io = IOProvider.getDefault().getIO(IO_NAME, false);
        try (OutputWriter out = io.getErr()) {
            out.println(message);
            out.flush();
        }
    }

    private class ChooseAction extends AbstractAction {

        private final JTextField display;

        private final FileFilter filter;

        public ChooseAction(JTextField display, FileFilter filter) {
            this.display = display;
            this.filter = filter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            if (filter instanceof DirFilter) {
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            } else {

            }
            fc.setFileFilter(filter);
            int ret = fc.showOpenDialog(OBuilderTopComponent.this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                display.setText(fc.getSelectedFile().getAbsolutePath());
                if (filter instanceof JarFilter) {
                    try {
                        while (getArtifactsModel().getRowCount() > 0) {
                            getArtifactsModel().removeRow(0);
                        }
                        Manifest m = new JarFile(fc.getSelectedFile()).getManifest();
                        BundleInfo info = ManifestParser.parseManifest(m);
                        Set<BundleRequirement> brs = info.getImports();
                        brs.forEach((br) -> {
                            getArtifactsModel().addRow(new Object[]{new StateValue(), br.getName(), br.getVersion(),
                                null, null, null});
                        });
                    } catch (IOException | ParseException ex) {
                        error(ex);
                    }
                    OBuilderStatusBar.STATUS.setIcon(StateRenderer.PROCESSING);
                    Set<MavenDependency> deps = new HashSet<>();
                    RequestProcessor.getDefault().execute(() -> {
                        int index = 0;
                        while (index != getArtifactsModel().getRowCount()) {
                            StateValue sv = (StateValue) getArtifactsModel().getValueAt(index, 0);
                            String ip = (String) getArtifactsModel().getValueAt(index, 1);
                            OBuilderStatusBar.STATUS.setText(ip);
                            try {
                                MavenDependency dep = searcher.search(ip, (VersionRange) getArtifactsModel().getValueAt(index, 2));
                                if (dep != null) {
                                    getArtifactsModel().setValueAt(dep.getGroupId(), index, 3);
                                    getArtifactsModel().setValueAt(dep.getArtifactId(), index, 4);
                                    getArtifactsModel().setValueAt(dep.getVersion(), index, 5);
                                    if (deps.contains(dep)) {
                                        sv.setState(StateValue.SKIP);
                                        info("Skipped to process " + ip + ".");
                                    } else {
                                        // TODO: BUILD IT BY MAVEN
                                        sv.setState(StateValue.SUCCESS);
                                        info("Succeeded to process " + ip + ".");
                                    }
                                    deps.add(dep);
                                } else {
                                    sv.setState(StateValue.FAILURE);
                                    error("Failed to process " + ip + ".");
                                }
                                
                            } catch (Exception ex) {
                                sv.setState(StateValue.FAILURE);
                                error(ex);
                            }
                            index++;
                            getArtifactsTable().repaint();
                        }
                        OBuilderStatusBar.clear();
                    });
                }
            }
        }
    }

    private static class JarFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().indexOf(".jar") == f.getName().length() - 4;
        }

        @Override
        public String getDescription() {
            return "*.jar";
        }

    }

    private static class DirFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory();
        }

        @Override
        public String getDescription() {
            return null;
        }

    }

    private static class StateRenderer extends DefaultTableCellRenderer {

        public static final ImageIcon PROCESSING = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/processing-16.gif"));

        private static final ImageIcon SUCCESS = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/success-16.png"));

        private static final ImageIcon FAILURE = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/failure-16.png"));
        
        private static final ImageIcon SKIP = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/skip-16.png"));

        private JPanel container;

        private JLabel bundle;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 0) {
                changeState((StateValue) value);
                return getContainer();
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        public void changeState(StateValue val) {
            if (val == null) {
                return;
            }
            if (val.getState() == StateValue.PROCESSING) {
                getBundle().setIcon(PROCESSING);
            } else if (val.getState() == StateValue.SUCCESS) {
                getBundle().setText(val.getBundle());
                getBundle().setIcon(SUCCESS);
            } else if (val.getState() == StateValue.FAILURE) {
                getBundle().setIcon(FAILURE);
            } else if (val.getState() == StateValue.SKIP) {
                getBundle().setIcon(SKIP);
            } else {
                getBundle().setIcon(null);
            }
        }

        private JPanel getContainer() {
            if (container == null) {
                container = new JPanel(new FlowLayout(FlowLayout.LEFT));
                container.add(getBundle());
                container.setBackground(Color.WHITE);
            }
            return container;
        }

        private JLabel getBundle() {
            if (bundle == null) {
                bundle = new JLabel();
                bundle.setBackground(Color.WHITE);
            }
            return bundle;
        }
    }
}
