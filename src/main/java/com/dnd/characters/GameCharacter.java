package com.dnd.characters;

import java.util.List;

import com.dnd.TranslationManager;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameCharacter {
    private List<String> feats;
    private List<String> languages;
    private List<String> proficiencies;
    private List<String> equipment;
    private String saveName;
    private int[] money;
    private int age;
    private int height;
    private int weight;    
    private int health;
    private int currentHealth;
    private int hitDie;
    private int exhaustion;
    private boolean[] conditions;
    private boolean fixedHealth;
    
    private final int[] skillAbilities;

    private final StringProperty name;
    private final StringProperty gender;
    private final StringProperty levelProperty;
    private final StringProperty classe;
    private final StringProperty subclass;
    private final StringProperty species;
    private final StringProperty lineage;
    private final StringProperty background;
    private final StringProperty alignment;
    private final StringProperty generationMethod;
    private final StringProperty[] abilityBaseProperties;

    private final IntegerProperty level;
    private final IntegerProperty proficiencyBonus;
    private final IntegerProperty generationPoints;
    private final IntegerProperty speed;
    private final IntegerProperty darkvision;
    private final IntegerProperty[] abilityBases;
    private final IntegerProperty[] abilityBonuses;
    private final IntegerProperty[] abilities;
    private final IntegerProperty[] abilityModifiers;
    private final IntegerProperty[] savingThrowBonuses;
    private final IntegerProperty[] savingThrowModifiers;
    private final IntegerProperty[] skillBonuses;
    private final IntegerProperty[] skillModifiers;

    private final BooleanProperty[] abilityPlusOnes;
    private final BooleanProperty[] abilityPlusTwos;
    private final BooleanProperty[] savingThrowProficiencies;
    private final BooleanProperty[] skillProficiencies;

    public GameCharacter() {
        String[] skillNames = getGroup(new String[] {"SKILLS"}); // Get the names of all skills
        int abilityCount = getGroup(new String[] {"ABILITIES"}).length;
        int skillCount = getGroup(new String[] {"SKILLS"}).length;

        // Initialize with default values
        skillAbilities = new int[skillCount];

        abilityBases = new IntegerProperty[abilityCount];
        abilityBonuses = new IntegerProperty[abilityCount];
        abilities = new IntegerProperty[abilityCount];
        abilityModifiers = new IntegerProperty[abilityCount];
        savingThrowBonuses = new IntegerProperty[abilityCount];
        savingThrowModifiers = new IntegerProperty[abilityCount];
        skillBonuses = new IntegerProperty[skillCount];
        skillModifiers = new IntegerProperty[skillCount];

        abilityPlusOnes = new BooleanProperty[abilityCount];
        abilityPlusTwos = new BooleanProperty[abilityCount];
        savingThrowProficiencies = new BooleanProperty[abilityCount];
        skillProficiencies = new BooleanProperty[skillCount];

        abilityBaseProperties = new StringProperty[abilityCount];

        levelProperty = new SimpleStringProperty("RANDOM");
        level = new SimpleIntegerProperty(0);
        proficiencyBonus = new SimpleIntegerProperty(1);

        bindLevel();
        bindProficiencyBonus();

        for (int i = 0; i < abilityBases.length; i++) {
            // Initialize each ability with a default value of 10
            abilityBases[i] = new SimpleIntegerProperty(0);

            abilityBaseProperties[i] = new SimpleStringProperty("RANDOM");

            // Initialize each abilityBonus with a default value of 0
            abilityBonuses[i] = new SimpleIntegerProperty(0);
            
            // Initialize each ability with a default value of 10
            abilities[i] = new SimpleIntegerProperty();

            // Initialize each abilityModifier and bind it to the corresponding ability
            abilityModifiers[i] = new SimpleIntegerProperty();
            
            bindAbilityBase(i);
            bindFinalAbility(i);
            bindAbilityModifier(i);

            abilityPlusOnes[i] = new SimpleBooleanProperty(false);
            abilityPlusTwos[i] = new SimpleBooleanProperty(false);

            savingThrowProficiencies[i] = new SimpleBooleanProperty(false);
            savingThrowBonuses[i] = new SimpleIntegerProperty(0);
            savingThrowModifiers[i] = new SimpleIntegerProperty();

            bindSavingThrowBonus(i);
            bindSavingThrowModifier(i);
        }

        for (int i = 0; i < skillBonuses.length; i++) {
            skillAbilities[i] = getInt(new String[] {"SKILLS", skillNames[i], "ABILITY"});
            skillProficiencies[i] = new SimpleBooleanProperty(false);
            skillBonuses[i] = new SimpleIntegerProperty(0);
            skillModifiers[i] = new SimpleIntegerProperty();

            bindSkillBonus(i);
            bindSkillModifier(i);
        }

        classe = new SimpleStringProperty("RANDOM");
        subclass = new SimpleStringProperty("RANDOM");

        species = new SimpleStringProperty("RANDOM");
        lineage = new SimpleStringProperty("RANDOM");
        background = new SimpleStringProperty("RANDOM");

        generationMethod = new SimpleStringProperty("STANDARD_ARRAY");
        generationPoints = new SimpleIntegerProperty(27); // Default value for point buy

        name = new SimpleStringProperty();
        gender = new SimpleStringProperty("RANDOM");
        alignment = new SimpleStringProperty("RANDOM");

        speed = new SimpleIntegerProperty(); // Default speed
        darkvision = new SimpleIntegerProperty(); // Default darkvision

        bindSpeed();
        bindDarkvision();
    }

    // Getters

    public StringProperty getClasse() {
        return classe;
    }

    public StringProperty getSpecies() {
        return species;
    }

    public StringProperty getLineage() {
        return lineage;
    }

    public StringProperty getSubclass() {
        return subclass;
    }

    public StringProperty getBackground() {
        return background;
    }

    public StringProperty getLevelProperty() {
        return levelProperty;
    }

    public StringProperty getGenerationMethod() {
        return generationMethod;
    }

    public StringProperty getAbilityBaseProperty(int index) {
        return abilityBaseProperties[index];
    }

    public StringProperty getName() {
        return name;
    }

    public StringProperty getGender() {
        return gender;
    }

    public StringProperty getAlignment() {
        return alignment;
    }

    public IntegerProperty getSpeed() {
        return speed;
    }

    public IntegerProperty getDarkvision() {
        return darkvision;
    }

    public IntegerProperty getAbilityBase(int index) {
        return abilityBases[index];
    }

    public IntegerProperty getAbilityModifier(int index) {
        return abilityModifiers[index];
    }

    public IntegerProperty getAbility(int index) {
        return abilities[index];
    }

    public IntegerProperty getSavingThrowModifier(int index) {
        return savingThrowModifiers[index];
    }

    public IntegerProperty getSkillModifier(int index) {
        return skillModifiers[index];
    }

    public IntegerProperty getGenerationPoints() {
        return generationPoints;
    }

    public BooleanProperty getAbilityPlusOne(int index) {
        return abilityPlusOnes[index];
    }

    public BooleanProperty getAbilityPlusTwo(int index) {
        return abilityPlusTwos[index];
    }

    public BooleanProperty getSavingThrowProficiency(int index) {
        return savingThrowProficiencies[index];
    }

    public BooleanProperty getSkillProficiency(int index) {
        return skillProficiencies[index];
    }

    // Setters

    public void resetAbilityBase(int i) {
        abilityBaseProperties[i].set("RANDOM");
    }

    public void setAbilityBase(int index, int value) {
        abilityBases[index].set(value);
    }

    public void setGenerationPoints(int value) {
        generationPoints.set(value);
    }

    public void setAbilityBaseProperty(int index, String value) {
        abilityBaseProperties[index].set(value);
    }

    // Adders

    public void addAbilityBonus(int index, int bonus) {
        abilityBonuses[index].set(abilityBonuses[index].get() + bonus);
    }

    // Binders

    private void bindFinalAbility(int index) {
        // Bind finalAbilities to track (abilityBases + abilityBonuses), defaulting to 10 if abilityBases[index] is 0
        abilities[index].bind(Bindings.createIntegerBinding(
            () -> {
                int base = abilityBases[index].get();
                int bonus = abilityBonuses[index].get();
                return (base == 0 ? 10 : base) + bonus; // Use 10 if base is 0
            },
            abilityBases[index], // Dependency on abilityBases[index]
            abilityBonuses[index] // Dependency on abilityBonuses[index]
        ));
    }

    private void bindAbilityModifier(int index) {
        // Bind the abilityModifier to the corresponding ability
        abilityModifiers[index].bind(abilities[index].add(-10).divide(2));
    }

    private void bindSavingThrowBonus(int index) {
        // Bind savingThrowBonuses[index] to include proficiencyBonus if savingThrowProficiency[index] is true
        savingThrowBonuses[index].bind(Bindings.createIntegerBinding(
            () -> savingThrowProficiencies[index].get() ? proficiencyBonus.get() : 0,
            savingThrowProficiencies[index],
            proficiencyBonus
        ));
    }

    private void bindSavingThrowModifier(int index) {
        // Bind the abilityModifier to the corresponding ability
        savingThrowModifiers[index].bind(abilityModifiers[index].add(savingThrowBonuses[index]));
    }

    private void bindSkillModifier(int index) {
        // Bind the skillModifier to the corresponding ability
        skillModifiers[index].bind(abilityModifiers[skillAbilities[index]].add(skillBonuses[index]));
    }

    private void bindSkillBonus(int index) {
        skillBonuses[index].bind(Bindings.createIntegerBinding(
            () -> skillProficiencies[index].get() ? proficiencyBonus.get() : 0,
            skillProficiencies[index],
            proficiencyBonus
        ));
    }

    private void bindSpeed() {
        speed.bind(Bindings.createIntegerBinding(
            () -> {
                int baseSpeed = getInt(new String[] {"SPECIES", species.get(), "SPEED"});
                return baseSpeed > 0 ? baseSpeed : 30;
            },
            species
        ));
    }

    private void bindDarkvision() {
        darkvision.bind(Bindings.createIntegerBinding(
            () -> {
                int baseDarkvision = getInt(new String[] {"SPECIES", species.get(), "DARKVISION"});
                return baseDarkvision > 0 ? baseDarkvision : 0;
            },
            species
        ));
    }

    private void bindProficiencyBonus() {
        proficiencyBonus.bind(level.add(7).divide(4));
    }

    private void bindLevel() {
        levelProperty.addListener((_, _, newValue) -> {
            if (newValue != null) {
                if (newValue.equalsIgnoreCase("RANDOM")) {
                    // Handle the "RANDOM" case explicitly
                    level.set(0); // Set level to 0 or any default value for "RANDOM"
                } else {
                    try {
                        // Attempt to parse the new value as an integer
                        int parsedValue = Integer.parseInt(newValue);
                        level.set(parsedValue); // Update the level property
                    } catch (NumberFormatException e) {
                        // Handle the case where the value is not a valid integer
                        System.err.println("Warning: Invalid level value: " + newValue);
                        level.set(0); // Set a default value (e.g., 0)
                    }
                } 
            } else {
                level.set(0); // Set a default value
            }
        });

        // Update `levelProperty` when `level` changes
        level.addListener((_, _, newValue) -> {
            if (newValue != null) {
                if (newValue.intValue() == 0) {
                    // If level is 0, set `levelProperty` to "RANDOM"
                    levelProperty.set("RANDOM");
                } else {
                    // Otherwise, set `levelProperty` to the string representation of the level
                    levelProperty.set(String.valueOf(newValue.intValue()));
                }
            }
        });
    }

    private void bindAbilityBase(int index) {
        abilityBaseProperties[index].addListener((_, _, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                if (newValue.equalsIgnoreCase("RANDOM")) {
                    // Handle the "RANDOM" case explicitly
                    abilityBases[index].set(0);
                } else {
                    try {
                        // Attempt to parse the new value as an integer
                        int parsedValue = Integer.parseInt(newValue);
                        abilityBases[index].set(parsedValue);
                    } catch (NumberFormatException e) {
                        // Handle the case where the value is not a valid integer
                        System.err.println("Warning: Invalid value: " + newValue);
                        abilityBases[index].set(10); // Set a default value
                    }
                }
            } else {
                abilityBases[index].set(0); // Set a default value
            }
        });

        abilityBases[index].addListener((_, _, newValue) -> {
            if (newValue != null) {
                if (newValue.intValue() == 0) {
                    abilityBaseProperties[index].set("RANDOM");
                } else {
                    abilityBaseProperties[index].set(String.valueOf(newValue.intValue()));
                }
            }
        });
    }

    // Helpers

    private String[] getGroup(String[] group) {
        return TranslationManager.getInstance().getGroup(group);
    }

    private int getInt(String[] group) {
        return TranslationManager.getInstance().getInt(group);
    }
}