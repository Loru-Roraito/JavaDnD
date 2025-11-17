package com.dnd.backend;

import com.dnd.frontend.language.TranslationManager;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.Proficiency;
import com.dnd.utils.items.Spell;
import com.google.gson.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

// TODO: give a look at the class as a whole. I have done the important parts, but I filled the rest with AI, so I need to check
public class CharacterSerializer {
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    public static class CharacterData {
        public String name;
        public String gender;
        public String species;
        public String lineage;
        public String[] classes;
        public String[] subclasses;
        public String background;
        public String[] levelsShown;
        public String alignment;
        public String size;
        public String languageOne;
        public String languageTwo;
        public String generationMethod;
        public String healthMethod;
        public String finesseAbility;
        public String userDescription;
        public int health;
        public int exhaustion;
        
        public int[] abilityBases;
        public boolean[] abilityPlusOnes;
        public boolean[] abilityPlusTwos;
        public boolean[] skillProficiencies;
        
        public String[][] feats;
        public String[][] featOnes;
        public String[][] featTwos;
        
        public String[] classEquipment;
        public String[] backgroundEquipment;
        
        public int[] moneys;
        public int[] availableSpellSlots;
        
        public boolean blinded;
        public boolean charmed;
        public boolean deafened;
        public boolean frightened;
        public boolean grappled;
        public boolean incapacitated;
        public boolean invisible;
        public boolean paralyzed;
        public boolean petrified;
        public boolean poisoned;
        public boolean prone;
        public boolean restrained;
        public boolean stunned;
        public boolean unconscious;
        
        public ProficiencyData[] choiceToolProficiencies;
        public SpellData[] spells;
        public SpellData[] cantrips;
        public ItemData[] items;

        public ItemData mainHand;
        public ItemData offHand;
        public ItemData armor;
        public ItemData shield;
    }

    public static class ItemData {
        public String nominative;
        
        public ItemData(String nominative) {
            this.nominative = nominative;
        }
    }
    
    public static class ProficiencyData {
        public String nominative;
        public String group;
        
        public ProficiencyData(String nominative, String group) {
            this.nominative = nominative;
            this.group = group;
        }
    }
    
    public static class SpellData {
        public String nominative;
        public String prepare;
        public String[] focus;
        public int ability;
        public Boolean limited;
        
        public SpellData(String nominative, String prepare, String[] focus, int ability, Boolean limited) {
            this.nominative = nominative;
            this.prepare = prepare;
            this.focus = focus;
            this.ability = ability;
            this.limited = limited;
        }
    }

    public static Boolean save(GameCharacter character, Boolean newFile, Stage stage) {
        File file;

        String currentFilePath = character.getPath();
        if (!currentFilePath.equals("") && !newFile) {
            file = new File(currentFilePath);
            character.setPath(currentFilePath);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(getTranslation("SAVE_CHARACTER"));
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(getTranslation("CHARACTER_FILES"), "*.dnd")
            );
            
            // Set initial directory to "saves" folder in program directory
            File savesDir = new File(System.getProperty("user.dir"), "saves");
            if (!savesDir.exists()) {
                savesDir.mkdirs(); // Create the directory if it doesn't exist
            }
            fileChooser.setInitialDirectory(savesDir);
            
            fileChooser.setInitialFileName(
                character.getSaveName().isEmpty() ? character.getName().get() + ".dnd" : character.getSaveName() + ".dnd"
            );
            
            file = fileChooser.showSaveDialog(stage);
        
            // Return false if user cancelled
            if (file == null) {
                return false;
            }

            character.setPath(file.getAbsolutePath());
            character.setSaveName(file.getName().replace(".dnd", ""));
        }
        
        try (Writer writer = new FileWriter(file)) {
            CharacterData data = new CharacterData();
            
            // Basic properties
            data.name = character.getName().get();
            data.gender = character.getGender().get();
            data.species = character.getSpecies().get();
            data.lineage = character.getLineage().get();
            data.background = character.getBackground().get();
            data.alignment = character.getAlignment().get();
            data.size = character.getSize().get();
            data.languageOne = character.getLanguageOne().get();
            data.languageTwo = character.getLanguageTwo().get();
            data.generationMethod = character.getGenerationMethod().get();
            data.healthMethod = character.getHealthMethod().get();
            data.finesseAbility = character.getFinesseAbility().get();
            data.userDescription = character.getUserDescription().get();
            data.health = character.getHealth().get();
            data.exhaustion = character.getExhaustion().get();
            
            // Abilities
            data.abilityBases = new int[6];
            data.abilityPlusOnes = new boolean[6];
            data.abilityPlusTwos = new boolean[6];
            for (int i = 0; i < 6; i++) {
                data.abilityBases[i] = character.getAbilityBase(i).get();
                data.abilityPlusOnes[i] = character.getAbilityPlusOne(i).get();
                data.abilityPlusTwos[i] = character.getAbilityPlusTwo(i).get();
            }
            
            // Skills
            data.skillProficiencies = new boolean[character.getSkillNames().length];
            for (int i = 0; i < character.getSkillNames().length; i++) {
                data.skillProficiencies[i] = character.getSkillProficiency(i).get();
            }
            
            data.classes = new String[character.getMaxClasses()];
            data.subclasses = new String[character.getMaxClasses()];
            data.levelsShown = new String[character.getMaxClasses()];
            // Feats
            data.feats = new String[character.getMaxClasses()][character.getMaxFeats()];
            data.featOnes = new String[character.getMaxClasses()][character.getMaxFeats()];
            data.featTwos = new String[character.getMaxClasses()][character.getMaxFeats()];
            for (int i = 0; i < character.getMaxClasses(); i++) {
                data.classes[i] = character.getClasse(i).get();
                data.subclasses[i] = character.getSubclass(i).get();
                data.levelsShown[i] = character.getLevelShown(i).get();
                for (int j = 0; j < character.getMaxFeats(); j++) {
                    data.feats[i][j] = character.getFeat(i, j).get();
                    data.featOnes[i][j] = character.getFeatOne(i, j).get();
                    data.featTwos[i][j] = character.getFeatTwo(i, j).get();
                }
            }
            
            // Equipment
            data.classEquipment = new String[character.getMaxSets() + 1];
            data.backgroundEquipment = new String[character.getMaxSets() + 1];
            for (int i = 0; i < character.getMaxSets() + 1; i++) {
                data.classEquipment[i] = character.getClassEquipment(i).get();
                data.backgroundEquipment[i] = character.getBackgroundEquipment(i).get();
            }
            
            // Money
            data.moneys = new int[5];
            for (int i = 0; i < 5; i++) {
                data.moneys[i] = character.getMoney(i).get();
            }
            
            // Spell slots
            data.availableSpellSlots = new int[9];
            for (int i = 0; i < 9; i++) {
                data.availableSpellSlots[i] = character.getAvailableSpellSlot(i).get();
            }
            
            // Conditions
            data.blinded = character.getBlinded().get();
            data.charmed = character.getCharmed().get();
            data.deafened = character.getDeafened().get();
            data.frightened = character.getFrightened().get();
            data.grappled = character.getGrappled().get();
            data.incapacitated = character.getIncapacitated().get();
            data.invisible = character.getInvisible().get();
            data.paralyzed = character.getParalyzed().get();
            data.petrified = character.getPetrified().get();
            data.poisoned = character.getPoisoned().get();
            data.prone = character.getProne().get();
            data.restrained = character.getRestrained().get();
            data.stunned = character.getStunned().get();
            data.unconscious = character.getUnconscious().get();
            
            // Proficiencies
            data.choiceToolProficiencies = character.getChoiceToolProficiencies().asList().stream()
                .map(p -> new ProficiencyData(p.getName(), p.getStrings()))
                .toArray(ProficiencyData[]::new);

            // Items
            data.items = character.getItems().asList().stream()
                .map(i -> new ItemData(i.getName()))
                .toArray(ItemData[]::new);

            // Spells
            data.spells = character.getSpells().asList().stream()
                .map(s -> new SpellData(s.getNominative(), s.getPrepare(), s.getFocus(), s.getAbility(), s.getLimited()))
                .toArray(SpellData[]::new);
            
            data.cantrips = character.getCantrips().asList().stream()
                .map(s -> new SpellData(s.getNominative(), s.getPrepare(), s.getFocus(), s.getAbility(), s.getLimited()))
                .toArray(SpellData[]::new);

            data.mainHand = new ItemData(character.getMainHand().get().getNominative());
            data.offHand = new ItemData(character.getOffHand().get().getNominative());
            data.armor = new ItemData(character.getArmor().get().getNominative());
            data.shield = new ItemData(character.getShield().get().getNominative());
            
            gson.toJson(data, writer);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving character: " + e.getMessage());
            return false;
        }
    }

    public static GameCharacter load(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getTranslation("LOAD_CHARACTER"));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(getTranslation("CHARACTER_FILES"), "*.dnd")
        );
            
        // Set initial directory to "saves" folder in program directory
        File savesDir = new File(System.getProperty("user.dir"), "saves");
        if (!savesDir.exists()) {
            savesDir.mkdirs(); // Create the directory if it doesn't exist
        }
        fileChooser.setInitialDirectory(savesDir);
        
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            try (Reader reader = new FileReader(file)) {
                CharacterData data = gson.fromJson(reader, CharacterData.class);
                GameCharacter character = new GameCharacter();
                
                // Load basic properties
                character.getName().set(data.name);
                character.getGender().set(data.gender);
                character.getSpecies().set(data.species);
                character.getLineage().set(data.lineage);
                character.getBackground().set(data.background);
                character.getAlignment().set(data.alignment);
                character.getSize().set(data.size);
                character.getLanguageOne().set(data.languageOne);
                character.getLanguageTwo().set(data.languageTwo);
                character.getGenerationMethod().set(data.generationMethod);
                character.getHealthMethod().set(data.healthMethod);
                character.getFinesseAbility().set(data.finesseAbility);
                character.getUserDescription().set(data.userDescription);
                character.getHealth().set(data.health);
                character.getExhaustion().set(data.exhaustion);
                
                // Load abilities
                for (int i = 0; i < 6; i++) {
                    character.getAbilityBase(i).set(data.abilityBases[i]);
                    character.getAbilityPlusOne(i).set(data.abilityPlusOnes[i]);
                    character.getAbilityPlusTwo(i).set(data.abilityPlusTwos[i]);
                }
                
                // Load skills
                for (int i = 0; i < data.skillProficiencies.length; i++) {
                    character.getSkillProficiency(i).set(data.skillProficiencies[i]);
                }
                
                for (int i = 0; i < data.classes.length; i++) {
                    character.getClasse(i).set(data.classes[i]);
                    character.getSubclass(i).set(data.subclasses[i]);
                    character.getLevelShown(i).set(data.levelsShown[i]);
                    // Load feats
                    for (int j = 0; j < data.feats[i].length; j++) {
                        character.getFeat(i, j).set(data.feats[i][j]);
                        character.getFeatOne(i, j).set(data.featOnes[i][j]);
                        character.getFeatTwo(i, j).set(data.featTwos[i][j]);
                    }
                }
                
                // Load equipment
                for (int i = 0; i < data.classEquipment.length; i++) {
                    character.getClassEquipment(i).set(data.classEquipment[i]);
                    character.getBackgroundEquipment(i).set(data.backgroundEquipment[i]);
                }
                
                // Load money
                for (int i = 0; i < 5; i++) {
                    character.getMoney(i).set(data.moneys[i]);
                }
                
                // Load spell slots
                for (int i = 0; i < 9; i++) {
                    character.getAvailableSpellSlot(i).set(data.availableSpellSlots[i]);
                }
                
                // Load conditions
                character.getBlinded().set(data.blinded);
                character.getCharmed().set(data.charmed);
                character.getDeafened().set(data.deafened);
                character.getFrightened().set(data.frightened);
                character.getGrappled().set(data.grappled);
                character.getIncapacitated().set(data.incapacitated);
                character.getInvisible().set(data.invisible);
                character.getParalyzed().set(data.paralyzed);
                character.getPetrified().set(data.petrified);
                character.getPoisoned().set(data.poisoned);
                character.getProne().set(data.prone);
                character.getRestrained().set(data.restrained);
                character.getStunned().set(data.stunned);
                character.getUnconscious().set(data.unconscious);
                
                // Load proficiencies and spells
                character.getChoiceToolProficiencies().getList().clear();
                for (ProficiencyData prof : data.choiceToolProficiencies) {
                    character.getChoiceToolProficiencies().add(new Proficiency(prof.nominative, prof.group));
                }

                character.getItems().getList().clear();
                for (ItemData item : data.items) {
                    character.getItems().add(new Item(item.nominative));
                }

                character.getSpells().getList().clear();
                for (SpellData spell : data.spells) {
                    character.getSpells().add(new Spell(spell.nominative, spell.prepare, spell.focus, spell.ability, spell.limited));
                }
                
                character.getCantrips().getList().clear();
                for (SpellData cantrip : data.cantrips) {
                    character.getCantrips().add(new Spell(cantrip.nominative, cantrip.prepare, cantrip.focus, cantrip.ability, cantrip.limited));
                }

                character.getMainHand().set(new Item(data.mainHand.nominative));
                character.getOffHand().set(new Item(data.offHand.nominative));
                character.getArmor().set(new Item(data.armor.nominative));
                character.getShield().set(new Item(data.shield.nominative));
                
                character.setPath(file.getAbsolutePath());
                character.setSaveName(file.getName().replace(".dnd", ""));
                return character;
                
            } catch (IOException e) {
                System.err.println("Error loading character: " + e.getMessage());
            }
        }
        
        return null;
    }

    private static String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}