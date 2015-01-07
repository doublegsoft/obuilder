/*
 * Copyright 2015 doublegsoft.net.
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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.doublegsoft.obuilder.MavenRun;
import net.doublegsoft.obuilder.manifest.BundleInfo;
import net.doublegsoft.obuilder.manifest.BundleRequirement;
import net.doublegsoft.obuilder.manifest.ManifestParser;
import net.doublegsoft.obuilder.manifest.VersionRange;
import net.doublegsoft.obuilder.search.MavenDependency;
import net.doublegsoft.obuilder.search.MavenSearcher;
import net.doublegsoft.obuilder.template.Template;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;
import org.openide.windows.TopComponent;

/**
 * It's the main UI for the application to operate.
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 *
 * @since 1.0
 */
@TopComponent.Description(preferredID = "OBuilderTopComponent", iconBase = "images/obuilder-16.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.OpenActionRegistration(displayName = "OBuilder", preferredID = "OBuilderTopComponent")
public class OBuilderTopComponent extends TopComponent {

    /**
     * Default IO name for netbeans output window.
     */
    private static final String IO_NAME = "OBuilder";

    /**
     * Default table cell renderer.
     */
    private static final StateRenderer STATE_RENDERER = new StateRenderer();

    /**
     * Searching maven dependencies.
     */
    private final MavenSearcher searcher = new MavenSearcher();

    /**
     * The utput directory text field.
     */
    private JTextField outputDir;

    /**
     * The choosing output directory button.
     */
    private JButton dirChoose;

    /**
     * The bundle jar text field.
     */
    private JTextField jarFile;

    /**
     * The choosing bundle jar text field.
     */
    private JButton jarChoose;

    /**
     * T build button.
     */
    private JButton build;

    /**
     * The table to display artifacts.
     */
    private JTable artifacts;

    /**
     * Creates a {@link JPanel} object with {@link FlowLayout} manager.
     *
     * @param components the components to layout
     *
     * @return a new {@link JPanel} object
     */
    private static JPanel flowPane(JComponent... components) {
        JPanel retVal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JComponent c : components) {
            retVal.add(c);
        }
        return retVal;
    }

    /**
     * Default constructor.
     */
    public OBuilderTopComponent() {
        setToolTipText("OBuilder");
        setDisplayName("OBuilder");
        initialize();
    }

    /**
     * Initializes UI.
     */
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

    /**
     * Gets the output directory text field lazily.
     *
     * @return the output directory text field
     */
    private JTextField getOutputDirTextField() {
        if (outputDir == null) {
            outputDir = new JTextField();
            outputDir.setEditable(false);
            outputDir.setPreferredSize(new Dimension(450, 28));
        }
        return outputDir;
    }

    /**
     * Gets the jar file text field lazily.
     *
     * @return the jar file text field
     */
    private JTextField getJarFileTextField() {
        if (jarFile == null) {
            jarFile = new JTextField();
            jarFile.setEditable(false);
            jarFile.setPreferredSize(new Dimension(450, 28));
        }
        return jarFile;
    }

    /**
     * Gets the choosing output directory button lazily.
     *
     * @return the choosing output directory button
     */
    private JButton getDirChooseButton() {
        if (dirChoose == null) {
            dirChoose = new JButton();
            dirChoose.setAction(new ChooseAction(getOutputDirTextField(), new DirFilter()));
            dirChoose.setText("...");
        }
        return dirChoose;
    }

    /**
     * Gets the choosing jar file button lazily.
     *
     * @return the choosing jar file button
     */
    private JButton getJarChooseButton() {
        if (jarChoose == null) {
            jarChoose = new JButton("...");
            jarChoose.setAction(new ChooseAction(getJarFileTextField(), new JarFilter()));
            jarChoose.setText("...");
        }
        return jarChoose;
    }

    /**
     * Gets the build button lazily.
     *
     * @return the build button
     */
    private JButton getBuildButton() {
        if (build == null) {
            build = new JButton();
            build.setAction(new BuildAction());
            build.setText("Build");
        }
        return build;
    }

    /**
     * Gets the artifacts table lazily.
     *
     * @return the artifacts table
     */
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

    /**
     * Gets the artifacts table model.
     *
     * @return the artifacts table model.
     */
    private DefaultTableModel getArtifactsModel() {
        return (DefaultTableModel) getArtifactsTable().getModel();
    }

    /**
     * Logs the informative message.
     *
     * @param message any informative message
     */
    private void info(String message) {
        InputOutput io = IOProvider.getDefault().getIO(IO_NAME, false);
        io.select();
        try (OutputWriter out = io.getOut()) {
            out.println(message);
            out.flush();
        }
    }

    /**
     * Logs the exception.
     *
     * @param cause any exception
     */
    private void error(Throwable cause) {
        InputOutput io = IOProvider.getDefault().getIO(IO_NAME, false);
        try (OutputWriter out = io.getErr()) {
            cause.printStackTrace(new PrintWriter(out));
            out.flush();
        }
    }

    /**
     * Logs the error message.
     *
     * @param message any error message
     */
    private void error(String message) {
        InputOutput io = IOProvider.getDefault().getIO(IO_NAME, false);
        try (OutputWriter out = io.getErr()) {
            out.println(message);
            out.flush();
        }
    }

    private int buildSingle(BundleData bd, File destin) throws IOException, InterruptedException {
        Template tpl = new Template();
        MavenRun run = new MavenRun();
        Map<String, Object> data = new HashMap<>();
        data.put("groupId", bd.getDependency().getGroupId());
        data.put("artifactId", bd.getDependency().getArtifactId());
        data.put("version", bd.getDependency().getVersion());
        data.put("bundleId", bd.getExportPackage());
        data.put("exportPackages", bd.getExportPackages());
        File pom = tpl.output(new File(destin, "tmp"), data);
        String mavenHome = NbPreferences.forModule(OBuilderOptionsPane.class).get("maven.home", "");
        return run.run(mavenHome, pom, (str) -> {
            info(str);
        });
    }

    private void resetStateValue(int state, Set<Integer> indexes, String bundle) {
        indexes.stream().map((idx) -> (StateValue) getArtifactsModel().getValueAt(idx, 0)).map((sv) -> {
            sv.setState(state);
            return sv;
        }).filter((sv) -> (state == StateValue.SUCCESS)).forEach((sv) -> {
            sv.setBundle(bundle);
        });
    }

    private void resetStateValue(int state, int index, String bundle) {
        StateValue sv = (StateValue) getArtifactsModel().getValueAt(index, 0);
        sv.setState(state);
        sv.setBundle(bundle);
    }

    /**
     * The action to choose file or directory.
     */
    private class ChooseAction extends AbstractAction {

        /**
         * The text field to diplay result.
         */
        private final JTextField display;

        /**
         * The file filter.
         */
        private final FileFilter filter;

        /**
         * Creates an action instance.
         *
         * @param display the text field to display choosing result
         *
         * @param filter file fileter to apply
         */
        public ChooseAction(JTextField display, FileFilter filter) {
            this.display = display;
            this.filter = filter;
        }

        /**
         * @see AbstractAction#actionPerformed(java.awt.event.ActionEvent)
         */
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

                }
            }
        }
    }

    /**
     * It's an action to build bundles.
     */
    private class BuildAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            String strDir = getOutputDirTextField().getText();
            if (strDir == null) {
                return;
            }
            File destin = new File(strDir);
            OBuilderStatusBar.STATUS.setIcon(StateRenderer.PROCESSING);
            Map<MavenDependency, BundleData> deps = new HashMap<>();
            List<MavenDependency> seqs = new ArrayList<>();
            RequestProcessor.getDefault().execute(() -> {
                BundleData data = null;
                int index = 0;
                while (index != getArtifactsModel().getRowCount()) {
                    String ip = (String) getArtifactsModel().getValueAt(index, 1);
                    OBuilderStatusBar.STATUS.setText("Searching for " + ip);
                    try {
                        MavenDependency dep = searcher.search(ip, (VersionRange) getArtifactsModel().getValueAt(index, 2));
                        if (dep != null) {
                            if (deps.containsKey(dep)) {
                                data = deps.get(dep);
                            } else {
                                data = new BundleData();
                                data.setDependency(dep);
                                deps.put(dep, data);
                            }
                            data.addIndex(index);
                            data.addExportPackage(ip);

                            getArtifactsModel().setValueAt(dep.getGroupId(), index, 3);
                            getArtifactsModel().setValueAt(dep.getArtifactId(), index, 4);
                            getArtifactsModel().setValueAt(dep.getVersion(), index, 5);

                            seqs.add(dep);
                        } else {
                            resetStateValue(StateValue.NOT_FOUND, index, null);
                            error("Not found " + ip + ".");
                        }
                    } catch (Exception ex) {
                        resetStateValue(StateValue.FAILURE, index, null);
                        error("Failed to process " + ip + ". Caused by: ");
                        error(ex);
                    }
                    index++;
                    getArtifactsTable().repaint();
                }
                seqs.forEach((seq) -> {
                    BundleData v = deps.get(seq);
                    OBuilderStatusBar.STATUS.setText("Building bundle for " + v.getExportPackages());
                    String name = v.getExportPackage() + "-bundle-" + v.getDependency().getVersion() + ".jar";
                    try {
                        if (buildSingle(v, destin) == 0) {
                            Files.copy(new File(destin, "tmp/target/" + name).toPath(), new File(destin, name).toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                            resetStateValue(StateValue.SUCCESS, v.getIndexes(), name);
                            info("Succeeded to build " + name + ".");
                        } else {
                            resetStateValue(StateValue.FAILURE, v.getIndexes(), null);
                            error("Failed to build " + name + ".");
                        }
                    } catch (IOException | InterruptedException ex) {
                        resetStateValue(StateValue.FAILURE, v.getIndexes(), null);
                        error("Failed to build " + name + ". Caused by: ");
                        error(ex);
                    }
                    getArtifactsTable().repaint();
                });
                OBuilderStatusBar.clear();
            });
        }

    }

    /**
     * The file filter to choose jar file only.
     */
    private static class JarFilter extends FileFilter {

        /**
         * @see FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().indexOf(".jar") == f.getName().length() - 4;
        }

        /**
         * @see FileFilter#getDescription()
         */
        @Override
        public String getDescription() {
            return "*.jar";
        }

    }

    /**
     * The file filter to choose directory only.
     */
    private static class DirFilter extends FileFilter {

        /**
         * @see FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(File f) {
            return f.isDirectory();
        }

        /**
         * @see FileFilter#getDescription()
         */
        @Override
        public String getDescription() {
            return null;
        }
    }

    /**
     * The table cell renderer to render the first column cells in the {@link #artifacts} table.
     */
    private static class StateRenderer extends DefaultTableCellRenderer {

        public static final ImageIcon PROCESSING = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/processing-16.gif"));

        private static final ImageIcon SUCCESS = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/success-16.png"));

        private static final ImageIcon FAILURE = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/failure-16.png"));

        private static final ImageIcon SKIP = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/skip-16.png"));

        private static final ImageIcon NOT_FOUND = new ImageIcon(StateRenderer.class.getClassLoader().getResource("images/notfound-16.png"));

        /**
         * The container to display
         */
        private JPanel container;

        /**
         * The label in the {@link #container}
         */
        private JLabel bundle;

        /**
         * @see DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean,
         * boolean, int, int)
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 0) {
                changeState((StateValue) value);
                return getContainer();
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        /**
         * Changes the icon according to the state value.
         *
         * @param val a new value
         */
        public void changeState(StateValue val) {
            if (val == null) {
                return;
            }
            if (val.getState() == StateValue.PROCESSING) {
                getBundle().setIcon(PROCESSING);
            } else if (val.getState() == StateValue.SUCCESS) {
                getBundle().setIcon(SUCCESS);
            } else if (val.getState() == StateValue.FAILURE) {
                getBundle().setIcon(FAILURE);
            } else if (val.getState() == StateValue.SKIP) {
                getBundle().setIcon(SKIP);
            } else if (val.getState() == StateValue.NOT_FOUND) {
                getBundle().setIcon(NOT_FOUND);
            } else {
                getBundle().setIcon(null);
            }
            getBundle().setText(val.getBundle());
        }

        /**
         * Gets the container.
         *
         * @return the real display component.
         */
        private JPanel getContainer() {
            if (container == null) {
                container = new JPanel(new FlowLayout(FlowLayout.LEFT));
                container.add(getBundle());
                container.setBackground(Color.WHITE);
            }
            return container;
        }

        /**
         * Gets the label in the {@link #container}.
         *
         * @return the label component
         */
        private JLabel getBundle() {
            if (bundle == null) {
                bundle = new JLabel();
                bundle.setBackground(Color.WHITE);
            }
            return bundle;
        }
    }
}
