package com.dnd;

import java.io.IOException;

import com.dnd.characters.GameCharacter;
import com.dnd.ui.tabs.CharacterTab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    private static TabPane mainTabPane;
    ViewModel character = new ViewModel(new GameCharacter());

    @Override
    public void start(Stage stage) throws IOException {
        TranslationManager.language = "it"; // Change the language to English

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

        // Get the screen dimensions
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Scale the TabPane based on screen size
        mainTabPane.setPrefWidth(screenWidth * 0.9);
        mainTabPane.setPrefHeight(screenHeight * 0.9);

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Bind font size to the width of the scene
        scene.widthProperty().addListener((_, _, newVal) -> {
            double fontSize = newVal.doubleValue() / 120; // Adjust divisor for scaling
            root.setStyle("-fx-font-size: " + fontSize + "px;");
        });

        stage.setTitle(TranslationManager.getInstance().getTranslation("MAIN_TITLE"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// fix ugly tooltips
// interfaccia per accedere a varie sezioni
// mostri
// aspetto estetico
// tiri per condizioni + condizioni attuali
// sistemare livelli talenti e impedire si superi una certa soglia di punti. Aggiungere talenti extra (possibilità di inserire talenti aggiuntivi)
// punti ferita temporanei
// dado vita
// denaro
// armi pesanti
// copertura
// armi versatili
// abilità extra talenti
// ispirazione eroica
// riposi brevi e lunghi
// affaticamento
// competenza e uso strumenti
// long rest resets hit point maximum, ability score reduction and exhaustion
// lancio incantesimi con livello maggiore
// preparare incantesimi
// currency conversion
// morte
// save advantages
// no spell se non competenza armature
// custom health
// average (medium) health
// aggiungere specifiche categorie di specie (es.: umanoidi, taglia, solo base)
// possibilità di comprimere alcune finestre
// sistemare dimensioni relative
// better differentiate which tab is active
// aggiungere tooltip
// automatic BASE_PATH
// scrollbar brutta, da sistemare
// iniziativa automatica (per mostri)
// avviso se si inserisce valore sbagliato
// avviso prima di levelUp
// calcolo peso trasportabile/trasportato
// riaggiungere freccette per nuovo equipaggiamento (va risolto il problema per cui inserisce l'oggetto due volte)
// aggiungere dizionario al ToolTipItemDelegate
// mostra dado di tiro e bonus facendo mouseover su dado
// PF aggiuntivi a livello devono essere almeno 1
// taglia modificabile
// custom abilities che si aggiorni in base alla classe quando non è selezionata
// chiudi tutto senza salvare
// ordinare elementi comboBox
// memorizza oggetti equipaggiati
// danno non può essere negativo
// scegliere tipo danno arma improvvisata
// riposo breve sblocca possibilità di usare dadi vita
// dizionario per buttons
// cosa succede se finiscono le competenze disponibili?
// elementi da riempire di colore diverso (es.: comboBox su casuale)
// salvare se abilità attiva è utilizzabile o meno (forse intendevo "in uso?", devo controllare)
// togliere duplicati da resistenze, ecc.
// dado più coreografico
// personaggio risulta modificato anche se non lo è. Evidentemente viene richiamato erroneamente il metodo dell'*
// link in dizionario non rimanda a stesso elemento
// add logger (to file)
// oggetti
// flavour a pagina 39/40
// equipaggiamento iniziale a livelli più alti
// trinket (pagina 46)
// minimo di 1 per dado vita
// abilità attive specie
// features talenti da magic initiate in poi
// niente magie in Ira
// multiclassing
// mix lignaggi/specie (eredità dai genitori) + schede parenti (usare tabelle di Xanathar)(definire funzione per vari parametri)
// generatore/caricatore di mappe
// simulazione combattimento
// magia
// app su telefono
// armi +1/elementali
// rendere utili borse nell'inventario. Drag and drop
// tasto destro per varie opzioni (come rimuovere oggetto o creare una borsa)
// rivendita oggetti
// sistemazione armatura di taglia sbagliata/rovinata
// sotto-sottorazze (es: sottorazze umano)
// allineamento che influenzi caratteristiche di lore
// tiri abilità con altre caratteristiche (es: storia(forza)). Probabilmente con tasto destro
// concentrazione
// add other image formats
// cambiare icona app
// image and txt saved as one file
// riordinare equipaggiamento
// drag and drop per le tab (eventualmente scrollbar orizzontale? Devo valurare)
// introdurre f-string?
// voci dizionario con nome di default
// non più di una copia della stessa voce del dizionario (se già presente, apri quella)
// LineEdit con possibilità di cliccare parole (e avere prima un tooltip)
// sottoclassi
// classi
// nomi d'infanzia (elfi)
// soprannomi (goliath)
// molteplici nomi (gnomi)
// custom abilities che si aggiorni in base alla classe ad ogni generazione (quindi che suggerisca le stat migliori) (in realtà probabilmente meglio fare solo il contrario)
// add bags to inventory (utile per equipaggiamento iniziale, per esempio). L'ideale sarebbe poter spostare gli oggetti col mouse (drag and drop, magari anche per le tab)
// tasto destro per avere varie opzioni (come rimuovere oggetto o creare una borsa)
// creature type (umanoide, ecc.)
// talenti che possono essere acquisiti solo una volta (non li ho già?)
// finesse a scelta (anche se inutile)
// heavy weapons
// secondo attacco con arma leggera puù avere bonus negativo
// rivendita oggetti/sistemazione misura armatura
// sottoclassi dal livello 3
// forza critico
// array per probabilità classe/razza dipendente da statistiche
// forse posso usare meglio i json
// spostare tutta la logica al back
// migliorare la logica di traduzione. Caricare tute le traduzioni subito e salvare il termine originale
// modificare la struttura delle abilità bonus (niente cose complicate, solamente lista e numero di abilità selezionabili)
// change male/female logic for translation
// migliorare custom binding/observable
// ordinare funzioni