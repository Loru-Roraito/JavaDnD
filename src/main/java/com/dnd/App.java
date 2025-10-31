package com.dnd;

import java.io.IOException;

import com.dnd.characters.GameCharacter;
import com.dnd.ui.tabs.CharacterTab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static TabPane mainTabPane;
    ViewModel character;

    @Override
    public void start(Stage stage) throws IOException {
        TranslationManager.language = "it"; // Change the language (relevant files need to be present in resources)
        character = new ViewModel(new GameCharacter(), stage);
        BorderPane root = new BorderPane();

        // Initialize the TabPane
        initializeTabPane();

        // Set the TabPane as the center of the layout
        root.setCenter(mainTabPane);

        // Initialize and set the scene
        initializeScene(stage, root);
    }

    private void initializeTabPane() {
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS); // Enable closable tabs

        // Add the generator tab
        CharacterTab generatorTab = new CharacterTab("GENERATOR_WINDOW", character, mainTabPane);
        mainTabPane.getTabs().add(generatorTab);
    }

    private void initializeScene(Stage stage, BorderPane root) {
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle(TranslationManager.getInstance().getTranslation("MAIN_TITLE"));
        stage.setScene(scene);

        // Get screen dimensions
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        
        // Set maximum size to screen size
        stage.setMaxWidth(bounds.getWidth());
        stage.setMaxHeight(bounds.getHeight());

        stage.setMaximized(true); // Maximized window (with title bar)

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// fix any todo
// interfaccia per accedere a varie sezioni
// mostri
// tiri per condizioni
// punti ferita temporanei
// dado vita
// armi pesanti
// copertura
// armi versatili
// ispirazione eroica
// riposi brevi e lunghi
// long rest resets hit point maximum, ability score reduction and exhaustion
// lancio incantesimi con livello maggiore
// currency conversion
// morte
// no spell se non competenza armature
// aggiungere specifiche categorie di specie (es.: umanoidi, taglia, solo base)
// finestra a parte con impostazioni generatore, spostare attuali elementi in SystemPane
// sistemare dimensioni relative (px in css resta fastidioso)
// better differentiate which tab is active
// iniziativa automatica (per mostri)
// avviso prima di levelUp
// calcolo peso trasportabile/trasportato
// PF aggiuntivi a livello devono essere almeno 1
// ordinare elementi comboBox
// oggetti equipaggiati
// danno non può essere negativo
// scegliere tipo danno arma improvvisata
// riposo breve sblocca possibilità di usare dadi vita
// elementi da riempire di colore diverso (es.: comboBox su casuale)
// togliere duplicati da resistenze, ecc.
// dado più coreografico
// add logger (to file)
// flavour a pagina 39/40
// equipaggiamento iniziale a livelli più alti
// trinket (pagina 46)
// minimo di 1 per dado vita
// abilità attive e passive
// multiclassing
// mix lignaggi/specie (eredità dai genitori) + schede parenti (usare tabelle di Xanathar)(definire funzione per vari parametri)
// generatore/caricatore di mappe
// simulazione combattimento
// app su telefono
// armi +1/elementali
// rendere utili borse nell'inventario. Drag and drop
// tasto destro per varie opzioni (come rimuovere oggetto o creare una borsa)
// aggiunta equipaggiamento
// rivendita oggetti
// sistemazione armatura di taglia sbagliata/rovinata
// tiri abilità con altre caratteristiche (es: storia(forza)). Probabilmente con tasto destro
// concentrazione
// add images
// image and txt saved as one file
// cambiare icona app
// riordinare equipaggiamento
// drag and drop per le tab (eventualmente scrollbar orizzontale? Devo valurare)
// abilità classi
// nomi d'infanzia (elfi)
// soprannomi (goliath)
// molteplici nomi (gnomi)
// creature type (umanoide, ecc.)
// finesse a scelta
// secondo attacco con arma leggera puù avere bonus negativo
// forza critico
// array per probabilità classe/razza dipendente da statistiche
// change male/female logic for translation
// tooltip per origine che mostra passive sbloccate e simili
// sistemare bene la questione del lignaggio degli gnomi/elfi. Voglio scrivere esplicitamente dei trucchetti posseduti
// possibilità di scegliere il tipo di calcolo CA
// further divide some files (especially groups.json and TranslationManager.java)
// rimuovere i listener quando elimino un oggetto
// magical focus
// packages (like adventurer's stuff)
// add item whose proficiency you selected (in case it applies)
// sound effects
// redesign HP for no class
// finish writing feats
// popup (critico, miss, debolezza, resistenza, immunità, vantaggio, svantaggio, ecc.)
// mostrare icone per effetti di stato in schermata principale
// peso aumenta sotto pietrificazione (in generale rivedere vari effetti di stato, come resistenza di pietrificazione)
// fighting styles
// potentially, change scrollbar background when disabled
// descriptions for epic boons, class abilities, spells
// possibilità di cliccare T su tooltip come in BG3
// make spell names in prepared selection clickable per selezionarli
// nascondere sezione magie se non utilizzate
// magia Warlock (al momento non è nemmeno in classi magiche)
// rendere spell levels scrollable
// tradurre materiali e descrizioni incantesimi
// Riempire ExtraTab
// pesi a random. Ed evitare random ripeta elementi
// spostare posizione competenze a scelta
// possibilità di regolare pesi random in tab apposita
// migliorare disposizione magie di livello
// aggiungere label per caratteristiche potenziate da talenti
// disegni Anastasia
// eventualmente sfondo
// continuare a sistemare estetica