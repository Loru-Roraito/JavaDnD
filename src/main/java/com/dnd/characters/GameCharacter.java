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
    private int currentHealth;
    private int exhaustion;
    private boolean[] conditions;

    private final int maxSubclasses;    
    private final int maxLineages;   
    private final int[] skillAbilities;

    private final String[] skillNames; // Get the names of all skills
    private final String[] abilitiesNames ;

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
    private final StringProperty healthMethod;
    private final StringProperty[] abilityBaseProperties;
    private final StringProperty[] availableSubclasses;
    private final StringProperty[] availableLineages;

    // I might want to switch to IntegerBinding for some of these, but there should be no issue in using IntegerProperty. Leaving as is for now.
    private final IntegerProperty level;
    private final IntegerProperty proficiencyBonus;
    private final IntegerProperty initiativeBonus;
    private final IntegerProperty generationPoints;
    private final IntegerProperty speed;
    private final IntegerProperty darkvision;
    private final IntegerProperty armorClass;
    private final IntegerProperty health;
    private final IntegerProperty hitDie;
    private final IntegerProperty givenBonuses; // Amount of bonuses already given
    private final IntegerProperty[] abilityBases;
    private final IntegerProperty[] abilityBonuses;
    private final IntegerProperty[] abilities;
    private final IntegerProperty[] abilityModifiers;
    private final IntegerProperty[] savingThrowBonuses;
    private final IntegerProperty[] savingThrowModifiers;
    private final IntegerProperty[] skillBonuses;
    private final IntegerProperty[] skillModifiers;

    private final BooleanProperty[] availableAbilities;
    private final BooleanProperty[] abilityPlusOnes;
    private final BooleanProperty[] abilityPlusTwos;
    private final BooleanProperty[] savingThrowProficiencies;
    private final BooleanProperty[] skillChoices;
    private final BooleanProperty[] skillProficiencies;
    private final BooleanProperty[] availableSkills;
    private final BooleanProperty[] fixedSkills;

    public GameCharacter() {
        skillNames = getGroup(new String[] {"skills"});
        abilitiesNames = getGroup(new String[] {"abilities"});
        int skillCount = skillNames.length;
        int abilityCount = abilitiesNames.length;

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
        skillChoices = new BooleanProperty[skillCount];

        abilityBaseProperties = new StringProperty[abilityCount];

        levelProperty = new SimpleStringProperty("RANDOM");
        level = new SimpleIntegerProperty(0);
        proficiencyBonus = new SimpleIntegerProperty();
        initiativeBonus = new SimpleIntegerProperty();

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

        givenBonuses = new SimpleIntegerProperty(0);
        bindGivenBonuses();

        background = new SimpleStringProperty("RANDOM");

        availableSkills = new BooleanProperty[skillCount];
        fixedSkills = new BooleanProperty[skillCount];
        bindAvailableSkills();
        bindFixedSkills();

        for (int i = 0; i < skillBonuses.length; i++) {
            skillAbilities[i] = getInt(new String[] {"skills", skillNames[i], "ability"});
            skillProficiencies[i] = new SimpleBooleanProperty(false);
            skillChoices[i] = new SimpleBooleanProperty(false);
            skillBonuses[i] = new SimpleIntegerProperty(0);
            skillModifiers[i] = new SimpleIntegerProperty();

            bindSkillBonus(i);
            bindSkillModifier(i);
            bindSkillProficiency(i);
        }

        classe = new SimpleStringProperty("RANDOM");
        subclass = new SimpleStringProperty("RANDOM");
        maxSubclasses = getInt(new String[] {"max_subclasses"});
        availableSubclasses = new StringProperty[maxSubclasses];
        bindAvailableSubclasses();

        species = new SimpleStringProperty("RANDOM");
        lineage = new SimpleStringProperty("RANDOM");
        maxLineages = getInt(new String[] {"max_lineages"});
        availableLineages = new StringProperty[maxLineages];
        bindAvailableLineages();

        generationMethod = new SimpleStringProperty("STANDARD_ARRAY");
        generationPoints = new SimpleIntegerProperty(27); // Default value for point buy
        healthMethod = new SimpleStringProperty("MEDIUM_HP");

        name = new SimpleStringProperty();
        gender = new SimpleStringProperty("RANDOM");
        alignment = new SimpleStringProperty("RANDOM");

        speed = new SimpleIntegerProperty();
        darkvision = new SimpleIntegerProperty();
        armorClass = new SimpleIntegerProperty();

        bindSpeed();
        bindDarkvision();
        bindArmorClass();
        bindInitiativeBonus();

        health = new SimpleIntegerProperty();
        hitDie = new SimpleIntegerProperty();

        bindHealth();
        bindHitDie();

        availableAbilities = new BooleanProperty[6];
        bindAvailableAbilities();
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

    public StringProperty getHealthMethod() {
        return healthMethod;
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

    public StringProperty[] getAvailableSubclasses() {
        return availableSubclasses;
    }

    public StringProperty[] getAvailableLineages() {
        return availableLineages;
    }

    public IntegerProperty getProficiencyBonus() {
        return proficiencyBonus;
    }

    public IntegerProperty getInitiativeBonus() {
        return initiativeBonus;
    }

    public IntegerProperty getSpeed() {
        return speed;
    }

    public IntegerProperty getDarkvision() {
        return darkvision;
    }

    public IntegerProperty getArmorClass() {
        return armorClass;
    }

    public IntegerProperty getHealth() {
        return health;
    }

    public IntegerProperty getGenerationPoints() {
        return generationPoints;
    }

    public IntegerProperty getGivenBonuses() {
        return givenBonuses;
    }

    public IntegerProperty getSkillModifier(int index) {
        return skillModifiers[index];
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

    public BooleanProperty getAvailableAbility(int index) {
        return availableAbilities[index];
    }

    public BooleanProperty getAvailableSkill(int index) {
        return availableSkills[index];
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

    public BooleanProperty getSkillChoice(int index) {
        return skillChoices[index];
    }

    public BooleanProperty getFixedSkill(int index) {
        return fixedSkills[index];
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

    private void bindAvailableSubclasses() {
        // Listen for changes to the 'classe' property and update availableSubclasses accordingly
        classe.addListener((_, _, newVal) -> {
            String[] subclasses = getGroup(new String[] {"classes", newVal, "subclasses"});
            for (int i = 0; i < availableSubclasses.length; i++) {
                if (subclasses != null && i < subclasses.length) {
                    availableSubclasses[i].set(subclasses[i]);
                } else {
                    availableSubclasses[i].set("");
                }
            }
        });

        for (int i = 0; i < availableSubclasses.length; i++) {
            availableSubclasses[i] = new SimpleStringProperty("");
        }
    }

    private void bindAvailableLineages() {
        species.addListener((_, _, newVal) -> {
            String[] lineages = getGroup(new String[] {"species", newVal, "lineages"});
            for (int i = 0; i < availableLineages.length; i++) {
                if (lineages != null && i < lineages.length) {
                    availableLineages[i].set(lineages[i]);
                } else {
                    availableLineages[i].set("");
                }
            }
        });

        for (int i = 0; i < availableLineages.length; i++) {
            availableLineages[i] = new SimpleStringProperty("");
        }
    }

    private void bindAvailableAbilities() {
        background.addListener((_, _, newVal) -> {
            String[] possibleAbilities = getGroup(new String[] {"backgrounds", newVal, "abilities"});
            for (int i = 0; i < availableAbilities.length; i++) {
                if (possibleAbilities != null && java.util.Arrays.asList(possibleAbilities).contains(abilitiesNames[i])) {
                    availableAbilities[i].set(true);
                } else {
                    availableAbilities[i].set(false); 
                }
            }
        });

        for (int i = 0; i < availableAbilities.length; i++) {
            availableAbilities[i] = new SimpleBooleanProperty(false);
        }
    }

    private void bindAvailableSkills() {
        for (int i = 0; i < availableSkills.length; i++) {
            availableSkills[i] = new SimpleBooleanProperty(false);
        }
    }

    private void bindFixedSkills() {
        background.addListener((_, _, newVal) -> {
            String[] possibleSkills = getGroup(new String[] {"backgrounds", newVal, "skills"});
            for (int i = 0; i < fixedSkills.length; i++) {
                if (possibleSkills != null && java.util.Arrays.asList(possibleSkills).contains(skillNames[i])) {
                    fixedSkills[i].set(true);
                } else {
                    fixedSkills[i].set(false);
                }
            }
        });

        for (int i = 0; i < fixedSkills.length; i++) {
            fixedSkills[i] = new SimpleBooleanProperty(false);
        }
    }

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

    private void bindSkillProficiency(int index) {
        // Bind the skillModifier to the corresponding ability
        skillProficiencies[index].bind(fixedSkills[index].or(skillChoices[index]));
    }

    private void bindGivenBonuses() {
        givenBonuses.bind(Bindings.createIntegerBinding(
            () -> {
                int total = 0;
                for (BooleanProperty plusOne : abilityPlusOnes) {
                    if (plusOne.get()) {
                        total++;
                    }
                }
                for (BooleanProperty plusTwo : abilityPlusTwos) {
                    if (plusTwo.get()) {
                        total += 2;
                    }
                }
                return total;
            },
            abilityPlusOnes[0], abilityPlusOnes[1], abilityPlusOnes[2], abilityPlusOnes[3], abilityPlusOnes[4], abilityPlusOnes[5],
            abilityPlusTwos[0], abilityPlusTwos[1], abilityPlusTwos[2], abilityPlusTwos[3], abilityPlusTwos[4], abilityPlusTwos[5]
        ));
    }

    private void bindSpeed() {
        speed.bind(Bindings.createIntegerBinding(
            () -> {
                int baseSpeed = getInt(new String[] {"species", species.get(), "speed"});
                return baseSpeed > 0 ? baseSpeed : 30;
            },
            species
        ));
    }

    private void bindDarkvision() {
        darkvision.bind(Bindings.createIntegerBinding(
            () -> {
                int baseDarkvision = getInt(new String[] {"species", species.get(), "darkvision"});
                return baseDarkvision > 0 ? baseDarkvision : 0;
            },
            species
        ));
    }

    private void bindArmorClass() {
        armorClass.bind(abilityModifiers[1].add(10));
    }

    private void bindProficiencyBonus() {
        proficiencyBonus.bind(level.add(7).divide(4));
    }

    private void bindInitiativeBonus() {
        initiativeBonus.bind(abilityModifiers[1]);
    }
    
    private void bindHealth(){
        health.bind((level.subtract(1)).multiply(hitDie.divide(2).add(1).add(abilityModifiers[2])).add(hitDie).add(abilityModifiers[2]));
    }

    private void bindHitDie(){
        hitDie.bind(Bindings.createIntegerBinding(
            () -> {
                int baseHitDie = getInt(new String[] {"classes", classe.get(), "hit_die"});
                return baseHitDie > 0 ? baseHitDie : 4;
            },
            classe
        ));
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