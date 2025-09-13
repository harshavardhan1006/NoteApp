public class Main {
    public static void main(String[] args) {
        NoteManager manager = new NoteManager("notes.txt");
        new NoteAppGUI(manager);
    }
}
