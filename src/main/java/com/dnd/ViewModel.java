package com.dnd;

import com.dnd.characters.GameCharacter;
import com.dnd.utils.Constants;
import com.dnd.utils.ObservableBoolean;
import com.dnd.utils.ObservableInteger;
import com.dnd.utils.ObservableString;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class ViewModel {
    private final StringProperty name;
    private final StringProperty gender;
    private final StringProperty subclass;
    private final StringProperty lineage;
    private final StringProperty alignment;
    private final StringProperty generationMethod;
    private final StringProperty healthMethod;
    private final StringProperty levelShown;
    private final StringProperty classe;
    private final StringProperty species;
    private final StringProperty background;
    private final StringProperty[] availableSubclasses;
    private final StringProperty[] availableLineages;
    private final StringProperty[] abilityBasesShown;

    private final IntegerProperty generationPoints;
    private final IntegerProperty initiativeBonus;
    private final IntegerProperty proficiencyBonus;
    private final IntegerProperty armorClass;
    private final IntegerProperty health;
    private final IntegerProperty[] abilities;
    private final IntegerProperty[] abilityModifiers;
    private final IntegerProperty[] savingThrowModifiers;
    private final IntegerProperty[] skillModifiers;
    private final IntegerProperty[] abilityBases;
    
    private final DoubleProperty speed;
    private final DoubleProperty darkvision;

    private final BooleanProperty[] availablePlusOnes;
    private final BooleanProperty[] availablePlusTwos;
    private final BooleanProperty[] availablePluses;
    private final BooleanProperty[] availableMinuses;
    private final BooleanProperty[] availableSkills;
    private final BooleanProperty[] abilityPlusOnes;
    private final BooleanProperty[] abilityPlusTwos;
    private final BooleanProperty[] savingThrowProficiencies;
    private final BooleanProperty[] skillProficiencies;
    
    private final ObservableList<StringProperty> actives;
    private final ObservableList<StringProperty> passives;

    public ViewModel(GameCharacter backend) {
        this.name = new SimpleStringProperty(getTranslation(backend.getName().get()));
        bindObservableString(name, backend.getName());

        this.gender = new SimpleStringProperty(getTranslation(backend.getGender().get()));
        bindObservableString(gender, backend.getGender());

        this.subclass = new SimpleStringProperty(getTranslation(backend.getSubclass().get()));
        bindObservableString(subclass, backend.getSubclass());

        this.lineage = new SimpleStringProperty(getTranslation(backend.getLineage().get()));
        bindObservableString(lineage, backend.getLineage());

        this.alignment = new SimpleStringProperty(getTranslation(backend.getAlignment().get()));
        bindObservableString(alignment, backend.getAlignment());

        this.generationMethod = new SimpleStringProperty(getTranslation(backend.getGenerationMethod().get()));
        bindObservableString(generationMethod, backend.getGenerationMethod());

        this.healthMethod = new SimpleStringProperty(getTranslation(backend.getHealthMethod().get()));
        bindObservableString(healthMethod, backend.getHealthMethod());

        this.levelShown = new SimpleStringProperty(getTranslation(backend.getLevelShown().get()));
        bindObservableString(levelShown, backend.getLevelShown());

        this.classe = new SimpleStringProperty(getTranslation(backend.getClasse().get()));
        bindObservableString(classe, backend.getClasse());

        this.species = new SimpleStringProperty(getTranslation(backend.getSpecies().get()));
        bindObservableString(species, backend.getSpecies());

        this.background = new SimpleStringProperty(getTranslation(backend.getBackground().get()));
        bindObservableString(background, backend.getBackground());
        
        this.generationPoints = new SimpleIntegerProperty(backend.getGenerationPoints().get());
        bindObservableInteger(generationPoints, backend.getGenerationPoints());

        this.initiativeBonus = new SimpleIntegerProperty(backend.getInitiativeBonus().get());
        bindObservableInteger(initiativeBonus, backend.getInitiativeBonus());

        this.proficiencyBonus = new SimpleIntegerProperty(backend.getProficiencyBonus().get());
        bindObservableInteger(proficiencyBonus, backend.getProficiencyBonus());

        this.speed = new SimpleDoubleProperty(backend.getSpeed().get() * Constants.LENGTH_MULTIPLIER);
        bindObservableDouble(speed, backend.getSpeed(), Constants.LENGTH_MULTIPLIER);

        this.darkvision = new SimpleDoubleProperty(backend.getDarkvision().get() * Constants.LENGTH_MULTIPLIER);
        bindObservableDouble(darkvision, backend.getDarkvision(), Constants.LENGTH_MULTIPLIER);

        this.armorClass = new SimpleIntegerProperty(backend.getArmorClass().get());
        bindObservableInteger(armorClass, backend.getArmorClass());

        this.health = new SimpleIntegerProperty(backend.getHealth().get());
        bindObservableInteger(health, backend.getHealth());

        int maxSubclasses = backend.getMaxSubclasses();
        int maxLineages = backend.getMaxLineages();

        this.availableSubclasses = new StringProperty[maxSubclasses];
        for (int i = 0; i < maxSubclasses; i++) {
            this.availableSubclasses[i] = new SimpleStringProperty(getTranslation(backend.getAvailableSubclass(i).get()));
            bindObservableString(availableSubclasses[i], backend.getAvailableSubclass(i));
        }

        this.availableLineages = new StringProperty[maxLineages];
        for (int i = 0; i < maxLineages; i++) {
            this.availableLineages[i] = new SimpleStringProperty(getTranslation(backend.getAvailableLineage(i).get()));
            bindObservableString(availableLineages[i], backend.getAvailableLineage(i));
        }

        String[] skillNames = backend.getSkillNames();
        String[] abilityNames = backend.getAbilityNames();

        this.abilityBasesShown = new StringProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.abilityBasesShown[i] = new SimpleStringProperty(getTranslation(backend.getAbilityBasesShown(i).get()));
            bindObservableString(abilityBasesShown[i], backend.getAbilityBasesShown(i));
        }

        this.abilities = new IntegerProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.abilities[i] = new SimpleIntegerProperty(backend.getAbility(i).get());
            bindObservableInteger(abilities[i], backend.getAbility(i));
        }

        this.abilityModifiers = new IntegerProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.abilityModifiers[i] = new SimpleIntegerProperty(backend.getAbilityModifier(i).get());
            bindObservableInteger(abilityModifiers[i], backend.getAbilityModifier(i));
        }

        this.savingThrowModifiers = new IntegerProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.savingThrowModifiers[i] = new SimpleIntegerProperty(backend.getSavingThrowModifier(i).get());
            bindObservableInteger(savingThrowModifiers[i], backend.getSavingThrowModifier(i));
        }

        this.skillModifiers = new IntegerProperty[skillNames.length];
        for (int i = 0; i < skillNames.length; i++) {
            this.skillModifiers[i] = new SimpleIntegerProperty(backend.getSkillModifier(i).get());
            bindObservableInteger(skillModifiers[i], backend.getSkillModifier(i));
        }

        this.abilityBases = new IntegerProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.abilityBases[i] = new SimpleIntegerProperty(backend.getAbilityBase(i).get());
            bindObservableInteger(abilityBases[i], backend.getAbilityBase(i));
        }
        
        this.availablePlusOnes = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.availablePlusOnes[i] = new SimpleBooleanProperty(backend.getAvailablePlusOne(i).get());
            bindObservableBoolean(availablePlusOnes[i], backend.getAvailablePlusOne(i));
        }

        this.availablePlusTwos = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.availablePlusTwos[i] = new SimpleBooleanProperty(backend.getAvailablePlusTwo(i).get());
            bindObservableBoolean(availablePlusTwos[i], backend.getAvailablePlusTwo(i));
        }

        this.availablePluses = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.availablePluses[i] = new SimpleBooleanProperty(backend.getAvailablePlus(i).get());
            bindObservableBoolean(availablePluses[i], backend.getAvailablePlus(i));
        }

        this.availableMinuses = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.availableMinuses[i] = new SimpleBooleanProperty(backend.getAvailableMinus(i).get());
            bindObservableBoolean(availableMinuses[i], backend.getAvailableMinus(i));
        }

        this.availableSkills = new BooleanProperty[skillNames.length];
        for (int i = 0; i < skillNames.length; i++) {
            this.availableSkills[i] = new SimpleBooleanProperty(backend.getAvailableSkill(i).get());
            bindObservableBoolean(availableSkills[i], backend.getAvailableSkill(i));
        }

        this.abilityPlusOnes = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.abilityPlusOnes[i] = new SimpleBooleanProperty(backend.getAbilityPlusOne(i).get());
            bindObservableBoolean(abilityPlusOnes[i], backend.getAbilityPlusOne(i));
        }

        this.abilityPlusTwos = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.abilityPlusTwos[i] = new SimpleBooleanProperty(backend.getAbilityPlusTwo(i).get());
            bindObservableBoolean(abilityPlusTwos[i], backend.getAbilityPlusTwo(i));
        }

        this.savingThrowProficiencies = new BooleanProperty[abilityNames.length];
        for (int i = 0; i < abilityNames.length; i++) {
            this.savingThrowProficiencies[i] = new SimpleBooleanProperty(backend.getSavingThrowProficiency(i).get());
            bindObservableBoolean(savingThrowProficiencies[i], backend.getSavingThrowProficiency(i));
        }

        this.skillProficiencies = new BooleanProperty[skillNames.length];
        for (int i = 0; i < skillNames.length; i++) {
            this.skillProficiencies[i] = new SimpleBooleanProperty(backend.getSkillProficiency(i).get());
            bindObservableBoolean(skillProficiencies[i], backend.getSkillProficiency(i));
        }

        this.actives = FXCollections.observableArrayList();

        // Helper to update the actives list
        Runnable updateActives = () -> {
            this.actives.clear();
            for (ObservableString key : backend.getActives()) {
                this.actives.add(new SimpleStringProperty(getTranslation(key.get())));
            }
        };

        // Listen for backend actives changes
        backend.getSpecies().addListener(_ -> updateActives.run());
        backend.getLineage().addListener(_ -> updateActives.run());
        backend.getLevel().addListener(_ -> updateActives.run());

        // Initialize once at construction
        updateActives.run();

        this.passives = FXCollections.observableArrayList();

        // Helper to update the passives list
        Runnable updatePassives = () -> {
            this.passives.clear();
            for (ObservableString key : backend.getPassives()) {
                this.passives.add(new SimpleStringProperty(getTranslation(key.get())));
            }
        };

        // Listen for backend passives changes
        backend.getSpecies().addListener(_ -> updatePassives.run());
        backend.getLineage().addListener(_ -> updatePassives.run());
        backend.getLevel().addListener(_ -> updatePassives.run());

        // Initialize once at construction
        updatePassives.run();
    }
    
    private void bindObservableString(StringProperty front, ObservableString back) {
        back.addListener(_ -> front.set(getTranslation(back.get())));
        front.addListener(_ -> back.set(getOriginal(front.get())));
    }

    private void bindObservableInteger(IntegerProperty front, ObservableInteger back) {
        back.addListener(_ -> front.set(back.get()));
        front.addListener(_ -> back.set(front.get()));
    }

    private void bindObservableDouble(DoubleProperty front, ObservableInteger back, double multiplier) {
        back.addListener(_ -> front.set(back.get() * multiplier));
        front.addListener(_ -> back.set((int) (front.get() / multiplier)));
    }

    private void bindObservableBoolean(BooleanProperty front, ObservableBoolean back) {
        back.addListener(_ -> front.set(back.get()));
        front.addListener(_ -> back.set(front.get()));
    }

    // Editors

    public void AbilityBasePlus(int index) {
        abilityBases[index].set(abilityBases[index].get() + 1);
    }

    public void AbilityBaseMinus(int index) {
        abilityBases[index].set(abilityBases[index].get() - 1);
    }

    // Getters for all properties

    public ObservableList<StringProperty> getActives() {
        return actives;
    }

    public ObservableList<StringProperty> getPassives() {
        return passives;
    }

    public StringProperty getName() {
        return name;
    }

    public StringProperty getGender() {
        return gender;
    }

    public StringProperty getSubclass() {
        return subclass;
    }

    public StringProperty getLineage() {
        return lineage;
    }

    public StringProperty getAlignment() {
        return alignment;
    }

    public StringProperty getGenerationMethod() {
        return generationMethod;
    }

    public StringProperty getHealthMethod() {
        return healthMethod;
    }

    public StringProperty getLevelShown() {
        return levelShown;
    }

    public StringProperty getClasse() {
        return classe;
    }

    public StringProperty getSpecies() {
        return species;
    }

    public StringProperty getBackground() {
        return background;
    }

    public StringProperty getAbilityBaseShown(int index) {
        return abilityBasesShown[index];
    }


    public StringProperty[] getAvailableSubclasses() {
        return availableSubclasses;
    }

    public StringProperty[] getAvailableLineages() {
        return availableLineages;
    }



    public IntegerProperty getGenerationPoints() {
        return generationPoints;
    }

    public IntegerProperty getInitiativeBonus() {
        return initiativeBonus;
    }

    public IntegerProperty getProficiencyBonus() {
        return proficiencyBonus;
    }

    public DoubleProperty getSpeed() {
        return speed;
    }

    public DoubleProperty getDarkvision() {
        return darkvision;
    }

    public IntegerProperty getArmorClass() {
        return armorClass;
    }

    public IntegerProperty getHealth() {
        return health;
    }

    public IntegerProperty getAbilityBase(int index) {
        return abilityBases[index];
    }

    public IntegerProperty getAbility(int index) {
        return abilities[index];
    }


    public IntegerProperty getAbilityModifier(int index) {
        return abilityModifiers[index];
    }

    public IntegerProperty getSavingThrowModifier(int index) {
        return savingThrowModifiers[index];
    }

    public IntegerProperty getSkillModifier(int index) {
        return skillModifiers[index];
    }
    

    public BooleanProperty getAvailableSkill(int index) {
        return availableSkills[index];
    }

    public BooleanProperty getAvailablePlusOne(int index) {
        return availablePlusOnes[index];
    }

    public BooleanProperty getAvailablePlusTwo(int index) {
        return availablePlusTwos[index];
    }

    public BooleanProperty getAvailablePlus(int index) {
        return availablePluses[index];
    }
    public BooleanProperty getAvailableMinus(int index) {
        return availableMinuses[index];
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


    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String getOriginal(String key) {
        return TranslationManager.getInstance().getOriginal(key);
    }
}