package com.dnd;

import java.util.List;

import com.dnd.characters.GameCharacter;
import com.dnd.utils.Constants;
import com.dnd.utils.CustomObservableList;
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
    private final StringProperty creatureType;
    private final StringProperty languageOne;
    private final StringProperty languageTwo;
    private final StringProperty height;
    private final StringProperty weight;
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
    private final StringProperty size;
    private final StringProperty originFeat;
    private final StringProperty[] moneysShown;
    private final StringProperty[] feats;
    private final StringProperty[] featOnes;
    private final StringProperty[] featTwos;
    private final StringProperty[] availableSizes;
    private final StringProperty[] availableSubclasses;
    private final StringProperty[] availableLineages;
    private final StringProperty[] abilityBasesShown;
    private final StringProperty[][] featAbilities;

    private final int maxFeats;

    private final IntegerProperty generationPoints;
    private final IntegerProperty initiativeBonus;
    private final IntegerProperty proficiencyBonus;
    private final IntegerProperty armorClass;
    private final IntegerProperty health;
    private final IntegerProperty fixedHealth;
    private final IntegerProperty hitDie;
    private final IntegerProperty availableFeats;
    private final IntegerProperty level;
    private final IntegerProperty exhaustion;
    private final IntegerProperty[] abilities;
    private final IntegerProperty[] abilityModifiers;
    private final IntegerProperty[] savingThrowModifiers;
    private final IntegerProperty[] skillModifiers;
    private final IntegerProperty[] abilityBases;
    
    // Maybe unnecessary? Int or Float could work? Right now I'll leave it like this, but is probably unoptimal (probably negligible, though).
    private final DoubleProperty speed;
    private final DoubleProperty darkvision;

    private final BooleanProperty blinded;
    private final BooleanProperty charmed;
    private final BooleanProperty deafened;
    private final BooleanProperty frightened;
    private final BooleanProperty grappled;
    private final BooleanProperty incapacitated;
    private final BooleanProperty incapacitation;
    private final BooleanProperty invisible;
    private final BooleanProperty paralyzed;
    private final BooleanProperty petrified;
    private final BooleanProperty poisoned;
    private final BooleanProperty prone;
    private final BooleanProperty proneness;
    private final BooleanProperty restrained;
    private final BooleanProperty stunned;
    private final BooleanProperty unconscious;

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
    private final ObservableList<StringProperty> weaponProficiencies;
    private final ObservableList<StringProperty> armorProficiencies;
    private final ObservableList<StringProperty> toolProficiencies;
    private final ObservableList<StringProperty> classEquipment;
    private final ObservableList<StringProperty> backgroundEquipment;
    private final ObservableList<StringProperty> choiceProficiencies;
    private final ObservableList<StringProperty> selectableFeats;

    public ViewModel(GameCharacter backend) {
        this.creatureType = new SimpleStringProperty(getTranslation(backend.getCreatureType().get()));
        bindObservableString(creatureType, backend.getCreatureType());

        this.languageOne = new SimpleStringProperty(getTranslation(backend.getLanguageOne().get()));
        bindObservableString(languageOne, backend.getLanguageOne());

        this.languageTwo = new SimpleStringProperty(getTranslation(backend.getLanguageTwo().get()));
        bindObservableString(languageTwo, backend.getLanguageTwo());

        this.height = new SimpleStringProperty(getTranslation(backend.getHeight().get()));
        bindObservableString(height, backend.getHeight());

        this.weight = new SimpleStringProperty(getTranslation(backend.getWeight().get()));
        bindObservableString(weight, backend.getWeight());

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

        this.fixedHealth = new SimpleIntegerProperty(backend.getFixedHealth().get());
        bindObservableInteger(fixedHealth, backend.getFixedHealth());

        this.hitDie = new SimpleIntegerProperty(backend.getHitDie().get());
        bindObservableInteger(hitDie, backend.getHitDie());

        this.level = new SimpleIntegerProperty(backend.getLevel().get());
        bindObservableInteger(level, backend.getLevel());

        this.exhaustion = new SimpleIntegerProperty(backend.getExhaustion().get());
        bindObservableInteger(exhaustion, backend.getExhaustion());

        this.size = new SimpleStringProperty(getTranslation(backend.getSize().get()));
        bindObservableString(size, backend.getSize());

        this.originFeat = new SimpleStringProperty(getTranslation(backend.getOriginFeat().get()));
        bindObservableString(originFeat, backend.getOriginFeat());

        this.blinded = new SimpleBooleanProperty(backend.getBlinded().get());
        bindObservableBoolean(blinded, backend.getBlinded());

        this.charmed = new SimpleBooleanProperty(backend.getCharmed().get());
        bindObservableBoolean(charmed, backend.getCharmed());

        this.deafened = new SimpleBooleanProperty(backend.getDeafened().get());
        bindObservableBoolean(deafened, backend.getDeafened());

        this.frightened = new SimpleBooleanProperty(backend.getFrightened().get());
        bindObservableBoolean(frightened, backend.getFrightened());

        this.grappled = new SimpleBooleanProperty(backend.getGrappled().get());
        bindObservableBoolean(grappled, backend.getGrappled());

        this.incapacitated = new SimpleBooleanProperty(backend.getIncapacitated().get());
        bindObservableBoolean(incapacitated, backend.getIncapacitated());

        this.incapacitation = new SimpleBooleanProperty(backend.getIncapacitation().get());
        bindObservableBoolean(incapacitation, backend.getIncapacitation());

        this.invisible = new SimpleBooleanProperty(backend.getInvisible().get());
        bindObservableBoolean(invisible, backend.getInvisible());

        this.paralyzed = new SimpleBooleanProperty(backend.getParalyzed().get());
        bindObservableBoolean(paralyzed, backend.getParalyzed());

        this.petrified = new SimpleBooleanProperty(backend.getPetrified().get());
        bindObservableBoolean(petrified, backend.getPetrified());

        this.poisoned = new SimpleBooleanProperty(backend.getPoisoned().get());
        bindObservableBoolean(poisoned, backend.getPoisoned());

        this.prone = new SimpleBooleanProperty(backend.getProne().get());
        bindObservableBoolean(prone, backend.getProne());

        this.proneness = new SimpleBooleanProperty(backend.getProneness().get());
        bindObservableBoolean(proneness, backend.getProneness());

        this.restrained = new SimpleBooleanProperty(backend.getRestrained().get());
        bindObservableBoolean(restrained, backend.getRestrained());

        this.stunned = new SimpleBooleanProperty(backend.getStunned().get());
        bindObservableBoolean(stunned, backend.getStunned());

        this.unconscious = new SimpleBooleanProperty(backend.getUnconscious().get());
        bindObservableBoolean(unconscious, backend.getUnconscious());

        this.moneysShown = new SimpleStringProperty[5];
        for (int i = 0; i < 5; i++) {
            this.moneysShown[i] = new SimpleStringProperty(getTranslation(backend.getMoneyShown(i).get()));
            bindObservableString(moneysShown[i], backend.getMoneyShown(i));
        }

        this.availableSizes = new StringProperty[2];
        for (int i = 0; i < 2; i++) {
            this.availableSizes[i] = new SimpleStringProperty(getTranslation(backend.getAvailableSize(i).get()));
            bindObservableString(availableSizes[i], backend.getAvailableSize(i));
        }

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

        int skillCount = backend.getSkillNames().length;
        int abilityCount = backend.getAbilityNames().length;

        this.abilityBasesShown = new StringProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.abilityBasesShown[i] = new SimpleStringProperty(getTranslation(backend.getAbilityBasesShown(i).get()));
            bindObservableString(abilityBasesShown[i], backend.getAbilityBasesShown(i));
        }

        this.abilities = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.abilities[i] = new SimpleIntegerProperty(backend.getAbility(i).get());
            bindObservableInteger(abilities[i], backend.getAbility(i));
        }

        this.abilityModifiers = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.abilityModifiers[i] = new SimpleIntegerProperty(backend.getAbilityModifier(i).get());
            bindObservableInteger(abilityModifiers[i], backend.getAbilityModifier(i));
        }

        this.savingThrowModifiers = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.savingThrowModifiers[i] = new SimpleIntegerProperty(backend.getSavingThrowModifier(i).get());
            bindObservableInteger(savingThrowModifiers[i], backend.getSavingThrowModifier(i));
        }

        this.skillModifiers = new IntegerProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            this.skillModifiers[i] = new SimpleIntegerProperty(backend.getSkillModifier(i).get());
            bindObservableInteger(skillModifiers[i], backend.getSkillModifier(i));
        }

        this.abilityBases = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.abilityBases[i] = new SimpleIntegerProperty(backend.getAbilityBase(i).get());
            bindObservableInteger(abilityBases[i], backend.getAbilityBase(i));
        }
        
        this.availablePlusOnes = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.availablePlusOnes[i] = new SimpleBooleanProperty(backend.getAvailablePlusOne(i).get());
            bindObservableBoolean(availablePlusOnes[i], backend.getAvailablePlusOne(i));
        }

        this.availablePlusTwos = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.availablePlusTwos[i] = new SimpleBooleanProperty(backend.getAvailablePlusTwo(i).get());
            bindObservableBoolean(availablePlusTwos[i], backend.getAvailablePlusTwo(i));
        }

        this.availablePluses = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.availablePluses[i] = new SimpleBooleanProperty(backend.getAvailablePlus(i).get());
            bindObservableBoolean(availablePluses[i], backend.getAvailablePlus(i));
        }

        this.availableMinuses = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.availableMinuses[i] = new SimpleBooleanProperty(backend.getAvailableMinus(i).get());
            bindObservableBoolean(availableMinuses[i], backend.getAvailableMinus(i));
        }

        this.availableSkills = new BooleanProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            this.availableSkills[i] = new SimpleBooleanProperty(backend.getAvailableSkill(i).get());
            bindObservableBoolean(availableSkills[i], backend.getAvailableSkill(i));
        }

        this.abilityPlusOnes = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.abilityPlusOnes[i] = new SimpleBooleanProperty(backend.getAbilityPlusOne(i).get());
            bindObservableBoolean(abilityPlusOnes[i], backend.getAbilityPlusOne(i));
        }

        this.abilityPlusTwos = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.abilityPlusTwos[i] = new SimpleBooleanProperty(backend.getAbilityPlusTwo(i).get());
            bindObservableBoolean(abilityPlusTwos[i], backend.getAbilityPlusTwo(i));
        }

        this.savingThrowProficiencies = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            this.savingThrowProficiencies[i] = new SimpleBooleanProperty(backend.getSavingThrowProficiency(i).get());
            bindObservableBoolean(savingThrowProficiencies[i], backend.getSavingThrowProficiency(i));
        }

        this.skillProficiencies = new BooleanProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            this.skillProficiencies[i] = new SimpleBooleanProperty(backend.getSkillProficiency(i).get());
            bindObservableBoolean(skillProficiencies[i], backend.getSkillProficiency(i));
        }

        this.actives = FXCollections.observableArrayList();
        updateList(actives, backend.getActives());

        this.passives = FXCollections.observableArrayList();
        updateList(passives, backend.getPassives());
        
        this.weaponProficiencies = FXCollections.observableArrayList();
        updateList(weaponProficiencies, backend.getWeaponProficiencies());

        this.armorProficiencies = FXCollections.observableArrayList();
        updateList(armorProficiencies, backend.getArmorProficiencies());

        this.classEquipment = FXCollections.observableArrayList();
        updateList(classEquipment, backend.getClassEquipment());

        this.backgroundEquipment = FXCollections.observableArrayList();
        updateList(backgroundEquipment, backend.getBackgroundEquipment());

        this.choiceProficiencies = FXCollections.observableArrayList();
        updateList(choiceProficiencies, backend.getChoiceProficiencies());

        this.toolProficiencies = FXCollections.observableArrayList();
        updateList(toolProficiencies, backend.getToolProficiencies());

        this.selectableFeats = FXCollections.observableArrayList();
        updateList(selectableFeats, backend.getNewSelectableFeats());

        maxFeats = backend.getMaxFeats();
        this.availableFeats = new SimpleIntegerProperty(backend.getAvailableFeats().get());
        bindObservableInteger(availableFeats, backend.getAvailableFeats());
        this.feats = new StringProperty[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            this.feats[i] = new SimpleStringProperty(getTranslation(backend.getFeat(i).get()));
            bindObservableString(feats[i], backend.getFeat(i));
        }

        this.featAbilities = new StringProperty[maxFeats][abilityCount];
        for (int i = 0; i < maxFeats; i++) {
            for (int j = 0; j < abilityCount; j++) {
                this.featAbilities[i][j] = new SimpleStringProperty(getTranslation(backend.getFeatAbility(i, j).get()));
                bindObservableString(featAbilities[i][j], backend.getFeatAbility(i, j));
            }
        }

        this.featOnes = new StringProperty[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            this.featOnes[i] = new SimpleStringProperty(getTranslation(backend.getFeatOne(i).get()));
            bindObservableString(featOnes[i], backend.getFeatOne(i));
        }

        this.featTwos = new StringProperty[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            this.featTwos[i] = new SimpleStringProperty(getTranslation(backend.getFeatTwo(i).get()));
            bindObservableString(featTwos[i], backend.getFeatTwo(i));
        }
    }

    private void updateList(ObservableList<StringProperty> front, CustomObservableList<ObservableString> back) {
        Runnable update = () -> {
            List<StringProperty> translated = new java.util.ArrayList<>();
            for (ObservableString key : back.getList()) {
                translated.add(new SimpleStringProperty(getTranslation(key.get())));
            }
            front.setAll(translated); // Only triggers listeners once
        };

        back.addListener(_ -> update.run());

        update.run();
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

    public ObservableList<StringProperty> getWeaponProficiencies() {
        return weaponProficiencies;
    }

    public ObservableList<StringProperty> getArmorProficiencies() {
        return armorProficiencies;
    }

    public ObservableList<StringProperty> getToolProficiencies() {
        return toolProficiencies;
    }

    public ObservableList<StringProperty> getSelectableFeats() {
        return selectableFeats;
    }

    public ObservableList<StringProperty> getClassEquipment() {
        return classEquipment;
    }

    public ObservableList<StringProperty> getBackgroundEquipment() {
        return backgroundEquipment;
    }

    public ObservableList<StringProperty> getChoiceProficiencies() {
        return choiceProficiencies;
    }

    public StringProperty getCreatureType() {
        return creatureType;
    }

    public StringProperty getLanguageOne() {
        return languageOne;
    }

    public StringProperty getLanguageTwo() {
        return languageTwo;
    }

    public StringProperty getHeight() {
        return height;
    }

    public StringProperty getWeight() {
        return weight;
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

    public StringProperty getSize() {
        return size;
    }

    public StringProperty getOriginFeat() {
        return originFeat;
    }

    public StringProperty getFeat(int index) {
        return feats[index];
    }

    public StringProperty getFeatOne(int index) {
        return featOnes[index];
    }

    public StringProperty getFeatTwo(int index) {
        return featTwos[index];
    }

    public StringProperty[] getFeatAbilities(int index) {
        return featAbilities[index];
    }

    public StringProperty[] getAvailableSizes() {
        return availableSizes;
    }


    public StringProperty[] getAvailableSubclasses() {
        return availableSubclasses;
    }

    public StringProperty[] getAvailableLineages() {
        return availableLineages;
    }


    public int getMaxFeats() {
        return maxFeats;
    }

    public IntegerProperty getExhaustion() {
        return exhaustion;
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

    public IntegerProperty getLevel() {
        return level;
    }

    public IntegerProperty getAvailableFeats() {
        return availableFeats;
    }

    public IntegerProperty getArmorClass() {
        return armorClass;
    }

    public IntegerProperty getHealth() {
        return health;
    }

    public IntegerProperty getFixedHealth() {
        return fixedHealth;
    }

    public IntegerProperty getHitDie() {
        return hitDie;
    }

    public StringProperty getMoneyShown(int index) {
        return moneysShown[index];
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


    public BooleanProperty getBlinded() {
        return blinded;
    }

    public BooleanProperty getCharmed() {
        return charmed;
    }

    public BooleanProperty getDeafened() {
        return deafened;
    }

    public BooleanProperty getFrightened() {
        return frightened;
    }

    public BooleanProperty getGrappled() {
        return grappled;
    }

    public BooleanProperty getIncapacitated() {
        return incapacitated;
    }

    public BooleanProperty getIncapacitation() {
        return incapacitation;
    }

    public BooleanProperty getInvisible() {
        return invisible;
    }

    public BooleanProperty getParalyzed() {
        return paralyzed;
    }

    public BooleanProperty getPetrified() {
        return petrified;
    }

    public BooleanProperty getPoisoned() {
        return poisoned;
    }

    public BooleanProperty getProne() {
        return prone;
    }

    public BooleanProperty getProneness() {
        return proneness;
    }

    public BooleanProperty getRestrained() {
        return restrained;
    }

    public BooleanProperty getStunned() {
        return stunned;
    }

    public BooleanProperty getUnconscious() {
        return unconscious;
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