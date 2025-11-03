package com.dnd.characters;

import com.dnd.items.Proficiency;
import com.dnd.items.Spell;
import com.dnd.TranslationManager;

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
        public String classe;
        public String subclass;
        public String background;
        public String levelShown;
        public String alignment;
        public String size;
        public String languageOne;
        public String languageTwo;
        public String generationMethod;
        public String healthMethod;
        public int health;
        public int exhaustion;
        
        public int[] abilityBases;
        public boolean[] abilityPlusOnes;
        public boolean[] abilityPlusTwos;
        public boolean[] skillProficiencies;
        
        public String[] feats;
        public String[] featOnes;
        public String[] featTwos;
        
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
        
        public ProficiencyData[] choiceProficiencies;
        public SpellData[] spells;
        public SpellData[] cantrips;
    }
    
    public static class ProficiencyData {
        public String name;
        public String group;
        
        public ProficiencyData(String name, String group) {
            this.name = name;
            this.group = group;
        }
    }
    
    public static class SpellData {
        public String nominative;
        public String prepare;
        public String[] focus;
        public int ability;
        public Boolean overriding;
        public Boolean limited;
        
        public SpellData(String nominative, String prepare, String[] focus, int ability, Boolean overriding, Boolean limited) {
            this.nominative = nominative;
            this.prepare = prepare;
            this.focus = focus;
            this.ability = ability;
            this.overriding = overriding;
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
            data.classe = character.getClasse().get();
            data.subclass = character.getSubclass().get();
            data.background = character.getBackground().get();
            data.levelShown = character.getLevelShown().get();
            data.alignment = character.getAlignment().get();
            data.size = character.getSize().get();
            data.languageOne = character.getLanguageOne().get();
            data.languageTwo = character.getLanguageTwo().get();
            data.generationMethod = character.getGenerationMethod().get();
            data.healthMethod = character.getHealthMethod().get();
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
            
            // Feats
            data.feats = new String[character.getMaxFeats()];
            data.featOnes = new String[character.getMaxFeats()];
            data.featTwos = new String[character.getMaxFeats()];
            for (int i = 0; i < character.getMaxFeats(); i++) {
                data.feats[i] = character.getFeat(i).get();
                data.featOnes[i] = character.getFeatOne(i).get();
                data.featTwos[i] = character.getFeatTwo(i).get();
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
            data.choiceProficiencies = character.getChoiceProficiencies().asList().stream()
                .map(p -> new ProficiencyData(p.getName(), p.getGroup()))
                .toArray(ProficiencyData[]::new);
            
            // Spells
            data.spells = character.getSpells().asList().stream()
                .map(s -> new SpellData(s.getNominative(), s.getPrepare(), s.getFocus(), s.getAbility(), s.getOverriding(), s.getLimited()))
                .toArray(SpellData[]::new);
            
            data.cantrips = character.getCantrips().asList().stream()
                .map(s -> new SpellData(s.getNominative(), s.getPrepare(), s.getFocus(), s.getAbility(), s.getOverriding(), s.getLimited()))
                .toArray(SpellData[]::new);
            
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
                character.getClasse().set(data.classe);
                character.getSubclass().set(data.subclass);
                character.getBackground().set(data.background);
                character.getLevelShown().set(data.levelShown);
                character.getAlignment().set(data.alignment);
                character.getSize().set(data.size);
                character.getLanguageOne().set(data.languageOne);
                character.getLanguageTwo().set(data.languageTwo);
                character.getGenerationMethod().set(data.generationMethod);
                character.getHealthMethod().set(data.healthMethod);
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
                
                // Load feats
                for (int i = 0; i < data.feats.length; i++) {
                    character.getFeat(i).set(data.feats[i]);
                    character.getFeatOne(i).set(data.featOnes[i]);
                    character.getFeatTwo(i).set(data.featTwos[i]);
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
                character.getChoiceProficiencies().getList().clear();
                for (ProficiencyData prof : data.choiceProficiencies) {
                    character.getChoiceProficiencies().add(new Proficiency(prof.name, prof.group));
                }
                
                character.getSpells().getList().clear();
                for (SpellData spell : data.spells) {
                    character.getSpells().add(new Spell(spell.nominative, spell.prepare, spell.focus, spell.ability, spell.overriding, spell.limited));
                }
                
                character.getCantrips().getList().clear();
                for (SpellData cantrip : data.cantrips) {
                    character.getCantrips().add(new Spell(cantrip.nominative, cantrip.prepare, cantrip.focus, cantrip.ability, cantrip.overriding, cantrip.limited));
                }
                
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
        return TranslationManager.getInstance().getTranslation(key);
    }
}