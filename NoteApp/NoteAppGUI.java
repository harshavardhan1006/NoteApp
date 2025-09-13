import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class NoteAppGUI extends JFrame {
    private NoteManager manager;
    private DefaultListModel<Note> listModel;
    private JList<Note> noteJList;
    private JTextField searchField; // 🔍 Search bar

    public NoteAppGUI(NoteManager manager) {
        this.manager = manager;

        setTitle("Note Taking App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🔍 Search bar at the top
        searchField = new JTextField(20);
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        noteJList = new JList<>(listModel);

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(new JScrollPane(noteJList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadNotes();

        addBtn.addActionListener(e -> openEditor(null));
        editBtn.addActionListener(e -> {
            Note selected = noteJList.getSelectedValue();
            if (selected != null) openEditor(selected);
        });
        deleteBtn.addActionListener(e -> {
            Note selected = noteJList.getSelectedValue();
            if (selected != null) {
                manager.deleteNote(selected);
                loadNotes();
            }
        });

        // 🔍 Filter notes when typing
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterNotes(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterNotes(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterNotes(); }
        });

        setVisible(true);
    }

    private void loadNotes() {
        listModel.clear();
        List<Note> notes = manager.getAllNotes();
        for (Note n : notes) listModel.addElement(n);
    }

    private void openEditor(Note existing) {
        JTextField titleField = new JTextField(20);
        JTextArea bodyArea = new JTextArea(10, 30);

        if (existing != null) {
            titleField.setText(existing.getTitle());
            bodyArea.setText(existing.getContent());
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Title:"), BorderLayout.NORTH);
        panel.add(titleField, BorderLayout.CENTER);
        panel.add(new JScrollPane(bodyArea), BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, panel,
                (existing == null ? "Add Note" : "Edit Note"),
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String body = bodyArea.getText().trim();
            if (existing == null) {
                manager.addNote(new Note(UUID.randomUUID().toString(), title, body));
            } else {
                existing.setTitle(title);
                existing.setContent(body);
                manager.updateNote(existing);
            }
            loadNotes();
            filterNotes(); // ✅ re-apply filter after save
        }
    }

    // 🔍 Method to filter notes based on search text
    private void filterNotes() {
        String query = searchField.getText().toLowerCase();
        listModel.clear();
        for (Note n : manager.getAllNotes()) {
            if (n.getTitle().toLowerCase().contains(query) ||
                n.getContent().toLowerCase().contains(query)) {
                listModel.addElement(n);
            }
        }
    }
}
