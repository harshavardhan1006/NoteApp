import java.util.*;
import java.io.*;

public class NoteManager {
    private List<Note> notes;
    private File dataFile;

    public NoteManager(String fileName) {
        notes = new ArrayList<>();
        dataFile = new File(fileName);
        loadNotes();
    }

    public synchronized List<Note> getAllNotes() {
        return new ArrayList<>(notes);
    }

    public synchronized void addNote(Note n) {
        notes.add(n);
        saveNotes();
    }

    public synchronized void updateNote(Note n) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(n.getId())) {
                notes.set(i, n);
                break;
            }
        }
        saveNotes();
    }

    public synchronized void deleteNote(Note n) {
        notes.remove(n);
        saveNotes();
    }

    private void loadNotes() {
        notes.clear();
        if (!dataFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 3);
                if (parts.length == 3) {
                    notes.add(new Note(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNotes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile))) {
            for (Note n : notes) {
                bw.write(n.getId() + ";" + n.getTitle() + ";" + n.getContent());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
