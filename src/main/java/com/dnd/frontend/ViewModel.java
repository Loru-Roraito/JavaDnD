package com.dnd.frontend;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dnd.backend.GameCharacter;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.MyItems;
import com.dnd.utils.items.Proficiency;
import com.dnd.utils.items.Spell;
import com.dnd.utils.observables.CustomObservableList;
import com.dnd.utils.observables.ObservableBoolean;
import com.dnd.utils.observables.ObservableInteger;
import com.dnd.utils.observables.ObservableItem;
import com.dnd.utils.observables.ObservableString;
import com.dnd.frontend.tabs.CharacterTab;
import com.dnd.frontend.language.Constants;
import com.dnd.frontend.language.TranslationManager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ViewModel {
    private final StringProperty userDescription;
    private final StringProperty finesseAbility;
    private final StringProperty saveName;
    private final StringProperty creatureType;
    private final StringProperty languageOne;
    private final StringProperty languageTwo;
    private final StringProperty height;
    private final StringProperty weight;
    private final StringProperty name;
    private final StringProperty gender;
    private final StringProperty lineage;
    private final StringProperty alignment;
    private final StringProperty generationMethod;
    private final StringProperty healthMethod;
    private final StringProperty species;
    private final StringProperty background;
    private final StringProperty size;
    private final StringProperty originFeat;
    private final StringProperty[] classes;
    private final StringProperty[] subclasses;
    private final StringProperty[] levelsShown;
    private final StringProperty[] spellcastingAbilities;
    private final StringProperty[] moneysShown;
    private final StringProperty[] availableSizes;
    private final StringProperty[] availableLineages;
    private final StringProperty[] abilityBasesShown;
    private final StringProperty[] classEquipment;
    private final StringProperty[] backgroundEquipment;
    private final StringProperty[][] availableSubclasses;
    private final StringProperty[][] feats;
    private final StringProperty[][] featOnes;
    private final StringProperty[][] featTwos;
    private final StringProperty[][][] featAbilities;

    private final int maxFeats;
    private final int maxClasses;
    private final int[] skillAbilities;

    private final IntegerProperty generationPoints;
    private final IntegerProperty initiativeBonus;
    private final IntegerProperty proficiencyBonus;
    private final IntegerProperty armorClass;
    private final IntegerProperty health;
    private final IntegerProperty fixedHealth;
    private final IntegerProperty exhaustion;
    private final IntegerProperty totalLevel;
    private final IntegerProperty[] hitDies;
    private final IntegerProperty[] availableFeats;
    private final IntegerProperty[] levels;
    private final IntegerProperty[] maxCantrips;
    private final IntegerProperty[] maxSpells;
    private final IntegerProperty[] spellcastingAbilityModifiers;
    private final IntegerProperty[] spellcastingAttackModifiers;
    private final IntegerProperty[] spellcastingSaveDCs;
    private final IntegerProperty[] abilities;
    private final IntegerProperty[] abilityModifiers;
    private final IntegerProperty[] savingThrowModifiers;
    private final IntegerProperty[] skillModifiers;
    private final IntegerProperty[] abilityBases;
    private final IntegerProperty[] spellSlots;
    private final IntegerProperty[] availableSpellSlots;
    
    // Maybe unnecessary? Int or Float could work? Right now I'll leave it like this, but is probably unoptimal (probably negligible, though).
    private final DoubleProperty speed;
    private final DoubleProperty darkvision;

    private final BooleanProperty isGenerator = new SimpleBooleanProperty(true);
    private final BooleanProperty isEditing = new SimpleBooleanProperty(true);

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
    private final BooleanProperty hasShieldProficiency;
    private final BooleanProperty hasArmorProficiency;
    private final BooleanProperty hasMainProficiency;
    private final BooleanProperty hasOffProficiency;

    private final BooleanProperty[] availablePlusOnes;
    private final BooleanProperty[] availablePlusTwos;
    private final BooleanProperty[] availablePluses;
    private final BooleanProperty[] availableMinuses;
    private final BooleanProperty[] availableSkills;
    private final BooleanProperty[] abilityPlusOnes;
    private final BooleanProperty[] abilityPlusTwos;
    private final BooleanProperty[] savingThrowProficiencies;
    private final BooleanProperty[] skillProficiencies;
    
    private final ObservableList<StringProperty> traits;
    private final ObservableList<StringProperty> weaponProficiencies;
    private final ObservableList<StringProperty> armorProficiencies;
    private final ObservableList<StringProperty> toolProficiencies;
    private final ObservableList<ObservableList<StringProperty>> selectableFeats;
    private final ObservableList<Proficiency> choiceToolProficiencies;
    private final ObservableList<ObservableList<Spell>> availableCantrips;
    private final ObservableList<ObservableList<Spell>> availableSpells;
    private final ObservableList<Spell> spells;
    private final ObservableList<Spell> cantrips;
    private final ObservableList<Item> items;

    private final ObservableItem mainHand;
    private final ObservableItem offHand;
    private final ObservableItem armor;
    private final ObservableItem shield;

    private final GameCharacter backend;
    private final Stage stage;
    private CharacterTab characterTab;

    public ViewModel(GameCharacter backend, Stage stage, CharacterTab characterTab) {
        this.stage = stage;
        this.backend = backend;
        this.characterTab = characterTab;

        skillAbilities = backend.getSkillAbilities();

        mainHand = new ObservableItem(backend.getMainHand().get());
        bindObservableItem(mainHand, backend.getMainHand());
        offHand = new ObservableItem(backend.getOffHand().get());
        bindObservableItem(offHand, backend.getOffHand());
        armor = new ObservableItem(backend.getArmor().get());
        bindObservableItem(armor, backend.getArmor());
        shield = new ObservableItem(backend.getShield().get());
        bindObservableItem(shield, backend.getShield());

        userDescription = new SimpleStringProperty(getTranslation(backend.getUserDescription().get()));
        bindObservableString(userDescription, backend.getUserDescription());

        finesseAbility = new SimpleStringProperty(getTranslation(backend.getFinesseAbility().get()));
        bindObservableString(finesseAbility, backend.getFinesseAbility());

        saveName = new SimpleStringProperty(backend.getSaveNameProperty().get());
        bindObservableString(saveName, backend.getSaveNameProperty());

        creatureType = new SimpleStringProperty(getTranslation(backend.getCreatureType().get()));
        bindObservableString(creatureType, backend.getCreatureType());

        languageOne = new SimpleStringProperty(getTranslation(backend.getLanguageOne().get()));
        bindObservableString(languageOne, backend.getLanguageOne());

        languageTwo = new SimpleStringProperty(getTranslation(backend.getLanguageTwo().get()));
        bindObservableString(languageTwo, backend.getLanguageTwo());

        height = new SimpleStringProperty(getTranslation(backend.getHeight().get()));
        bindObservableString(height, backend.getHeight());

        weight = new SimpleStringProperty(getTranslation(backend.getWeight().get()));
        bindObservableString(weight, backend.getWeight());

        name = new SimpleStringProperty(getTranslation(backend.getName().get()));
        bindObservableString(name, backend.getName());

        gender = new SimpleStringProperty(getTranslation(backend.getGender().get()));
        bindObservableString(gender, backend.getGender());

        lineage = new SimpleStringProperty(getTranslation(backend.getLineage().get()));
        bindObservableString(lineage, backend.getLineage());

        alignment = new SimpleStringProperty(getTranslation(backend.getAlignment().get()));
        bindObservableString(alignment, backend.getAlignment());

        generationMethod = new SimpleStringProperty(getTranslation(backend.getGenerationMethod().get()));
        bindObservableString(generationMethod, backend.getGenerationMethod());

        healthMethod = new SimpleStringProperty(getTranslation(backend.getHealthMethod().get()));
        bindObservableString(healthMethod, backend.getHealthMethod());

        totalLevel = new SimpleIntegerProperty(backend.getTotalLevel().get());
        bindObservableInteger(totalLevel, backend.getTotalLevel());

        species = new SimpleStringProperty(getTranslation(backend.getSpecies().get()));
        bindObservableString(species, backend.getSpecies());

        background = new SimpleStringProperty(getTranslation(backend.getBackground().get()));
        bindObservableString(background, backend.getBackground());

        generationPoints = new SimpleIntegerProperty(backend.getGenerationPoints().get());
        bindObservableInteger(generationPoints, backend.getGenerationPoints());

        initiativeBonus = new SimpleIntegerProperty(backend.getInitiativeBonus().get());
        bindObservableInteger(initiativeBonus, backend.getInitiativeBonus());

        proficiencyBonus = new SimpleIntegerProperty(backend.getProficiencyBonus().get());
        bindObservableInteger(proficiencyBonus, backend.getProficiencyBonus());

        speed = new SimpleDoubleProperty(backend.getSpeed().get() * Constants.LENGTH_MULTIPLIER);
        bindObservableDouble(speed, backend.getSpeed(), Constants.LENGTH_MULTIPLIER);

        darkvision = new SimpleDoubleProperty(backend.getDarkvision().get() * Constants.LENGTH_MULTIPLIER);
        bindObservableDouble(darkvision, backend.getDarkvision(), Constants.LENGTH_MULTIPLIER);

        armorClass = new SimpleIntegerProperty(backend.getArmorClass().get());
        bindObservableInteger(armorClass, backend.getArmorClass());

        health = new SimpleIntegerProperty(backend.getHealth().get());
        bindObservableInteger(health, backend.getHealth());

        fixedHealth = new SimpleIntegerProperty(backend.getFixedHealth().get());
        bindObservableInteger(fixedHealth, backend.getFixedHealth());

        exhaustion = new SimpleIntegerProperty(backend.getExhaustion().get());
        bindObservableInteger(exhaustion, backend.getExhaustion());

        size = new SimpleStringProperty(getTranslation(backend.getSize().get()));
        bindObservableString(size, backend.getSize());

        originFeat = new SimpleStringProperty(getTranslation(backend.getOriginFeat().get()));
        bindObservableString(originFeat, backend.getOriginFeat());

        blinded = new SimpleBooleanProperty(backend.getBlinded().get());
        bindObservableBoolean(blinded, backend.getBlinded());

        charmed = new SimpleBooleanProperty(backend.getCharmed().get());
        bindObservableBoolean(charmed, backend.getCharmed());

        deafened = new SimpleBooleanProperty(backend.getDeafened().get());
        bindObservableBoolean(deafened, backend.getDeafened());

        frightened = new SimpleBooleanProperty(backend.getFrightened().get());
        bindObservableBoolean(frightened, backend.getFrightened());

        grappled = new SimpleBooleanProperty(backend.getGrappled().get());
        bindObservableBoolean(grappled, backend.getGrappled());

        incapacitated = new SimpleBooleanProperty(backend.getIncapacitated().get());
        bindObservableBoolean(incapacitated, backend.getIncapacitated());

        incapacitation = new SimpleBooleanProperty(backend.getIncapacitation().get());
        bindObservableBoolean(incapacitation, backend.getIncapacitation());

        invisible = new SimpleBooleanProperty(backend.getInvisible().get());
        bindObservableBoolean(invisible, backend.getInvisible());

        paralyzed = new SimpleBooleanProperty(backend.getParalyzed().get());
        bindObservableBoolean(paralyzed, backend.getParalyzed());

        petrified = new SimpleBooleanProperty(backend.getPetrified().get());
        bindObservableBoolean(petrified, backend.getPetrified());

        poisoned = new SimpleBooleanProperty(backend.getPoisoned().get());
        bindObservableBoolean(poisoned, backend.getPoisoned());

        prone = new SimpleBooleanProperty(backend.getProne().get());
        bindObservableBoolean(prone, backend.getProne());

        proneness = new SimpleBooleanProperty(backend.getProneness().get());
        bindObservableBoolean(proneness, backend.getProneness());

        restrained = new SimpleBooleanProperty(backend.getRestrained().get());
        bindObservableBoolean(restrained, backend.getRestrained());

        stunned = new SimpleBooleanProperty(backend.getStunned().get());
        bindObservableBoolean(stunned, backend.getStunned());

        unconscious = new SimpleBooleanProperty(backend.getUnconscious().get());
        bindObservableBoolean(unconscious, backend.getUnconscious());

        hasShieldProficiency = new SimpleBooleanProperty(backend.hasShieldProficiency().get());
        bindObservableBoolean(hasShieldProficiency, backend.hasShieldProficiency());

        hasArmorProficiency = new SimpleBooleanProperty(backend.hasArmorProficiency().get());
        bindObservableBoolean(hasArmorProficiency, backend.hasArmorProficiency());

        hasMainProficiency = new SimpleBooleanProperty(backend.hasMainProficiency().get());
        bindObservableBoolean(hasMainProficiency, backend.hasMainProficiency());

        hasOffProficiency = new SimpleBooleanProperty(backend.hasOffProficiency().get());
        bindObservableBoolean(hasOffProficiency, backend.hasOffProficiency());

        maxClasses = backend.getMaxClasses();
        levelsShown = new SimpleStringProperty[maxClasses];
        classes = new SimpleStringProperty[maxClasses];
        subclasses = new SimpleStringProperty[maxClasses];
        hitDies = new SimpleIntegerProperty[maxClasses];
        levels = new SimpleIntegerProperty[maxClasses];
        maxCantrips = new SimpleIntegerProperty[maxClasses];
        maxSpells = new SimpleIntegerProperty[maxClasses];
        spellcastingAbilityModifiers = new SimpleIntegerProperty[maxClasses];
        spellcastingAttackModifiers = new SimpleIntegerProperty[maxClasses];
        spellcastingSaveDCs = new SimpleIntegerProperty[maxClasses];
        spellcastingAbilities = new SimpleStringProperty[maxClasses];

        for (int i = 0; i < maxClasses; i++) {
            levelsShown[i] = new SimpleStringProperty(getTranslation(backend.getLevelShown(i).get()));
            bindObservableString(levelsShown[i], backend.getLevelShown(i));

            classes[i] = new SimpleStringProperty(getTranslation(backend.getClasse(i).get()));
            bindObservableString(classes[i], backend.getClasse(i));

            subclasses[i] = new SimpleStringProperty(getTranslation(backend.getSubclass(i).get()));
            bindObservableString(subclasses[i], backend.getSubclass(i));

            hitDies[i] = new SimpleIntegerProperty(backend.getHitDie(i).get());
            bindObservableInteger(hitDies[i], backend.getHitDie(i));

            levels[i] = new SimpleIntegerProperty(backend.getLevel(i).get());
            bindObservableInteger(levels[i], backend.getLevel(i));

            maxCantrips[i] = new SimpleIntegerProperty(backend.getMaxCantrips(i).get());
            bindObservableInteger(maxCantrips[i], backend.getMaxCantrips(i));

            maxSpells[i] = new SimpleIntegerProperty(backend.getMaxSpells(i).get());
            bindObservableInteger(maxSpells[i], backend.getMaxSpells(i));

            spellcastingAbilityModifiers[i] = new SimpleIntegerProperty(backend.getSpellcastingAbilityModifier(i).get());
            bindObservableInteger(spellcastingAbilityModifiers[i], backend.getSpellcastingAbilityModifier(i));

            spellcastingAttackModifiers[i] = new SimpleIntegerProperty(backend.getSpellcastingAttackModifier(i).get());
            bindObservableInteger(spellcastingAttackModifiers[i], backend.getSpellcastingAttackModifier(i));

            spellcastingSaveDCs[i] = new SimpleIntegerProperty(backend.getSpellcastingSaveDC(i).get());
            bindObservableInteger(spellcastingSaveDCs[i], backend.getSpellcastingSaveDC(i));

            spellcastingAbilities[i] = new SimpleStringProperty(getTranslation(backend.getSpellcastingAbility(i).get()));
            bindObservableString(spellcastingAbilities[i], backend.getSpellcastingAbility(i));
        }

        moneysShown = new SimpleStringProperty[5];
        for (int i = 0; i < 5; i++) {
            moneysShown[i] = new SimpleStringProperty(getTranslation(backend.getMoneyShown(i).get()));
            bindObservableString(moneysShown[i], backend.getMoneyShown(i));
        }

        availableSizes = new StringProperty[2];
        for (int i = 0; i < 2; i++) {
            availableSizes[i] = new SimpleStringProperty(getTranslation(backend.getAvailableSize(i).get()));
            bindObservableString(availableSizes[i], backend.getAvailableSize(i));
        }

        int maxSubclasses = backend.getMaxSubclasses();
        int maxLineages = backend.getMaxLineages();
        int maxSets = backend.getMaxSets();

        availableSubclasses = new StringProperty[maxClasses][maxSubclasses];
        for (int i = 0; i < maxClasses; i++) {
            for (int j = 0; j < maxSubclasses; j++) {
                availableSubclasses[i][j] = new SimpleStringProperty(getTranslation(backend.getAvailableSubclass(i, j).get()));
                bindObservableString(availableSubclasses[i][j], backend.getAvailableSubclass(i, j));
            }
        }

        availableLineages = new StringProperty[maxLineages];
        for (int i = 0; i < maxLineages; i++) {
            availableLineages[i] = new SimpleStringProperty(getTranslation(backend.getAvailableLineage(i).get()));
            bindObservableString(availableLineages[i], backend.getAvailableLineage(i));
        }

        classEquipment = new StringProperty[maxSets];
        for (int i = 0; i < maxSets; i++) {
            classEquipment[i] = new SimpleStringProperty(getTranslation(backend.getClassEquipment(i).get()));
            bindObservableString(classEquipment[i], backend.getClassEquipment(i));
        }

        backgroundEquipment = new StringProperty[maxSets];
        for (int i = 0; i < maxSets; i++) {
            backgroundEquipment[i] = new SimpleStringProperty(getTranslation(backend.getBackgroundEquipment(i).get()));
            bindObservableString(backgroundEquipment[i], backend.getBackgroundEquipment(i));
        }

        spellSlots = new IntegerProperty[9];
        for (int i = 0; i < 9; i ++) {
            spellSlots[i] = new SimpleIntegerProperty(backend.getSpellSlot(i).get());
            bindObservableInteger(spellSlots[i], backend.getSpellSlot(i));
        }

        availableSpellSlots = new IntegerProperty[9];
        for (int i = 0; i < 9; i ++) {
            availableSpellSlots[i] = new SimpleIntegerProperty(backend.getAvailableSpellSlot(i).get());
            bindObservableInteger(availableSpellSlots[i], backend.getAvailableSpellSlot(i));
        }

        int skillCount = backend.getSkillNames().length;
        int abilityCount = backend.getAbilityNames().length;

        abilityBasesShown = new StringProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilityBasesShown[i] = new SimpleStringProperty(getTranslation(backend.getAbilityBasesShown(i).get()));
            bindObservableString(abilityBasesShown[i], backend.getAbilityBasesShown(i));
        }

        abilities = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilities[i] = new SimpleIntegerProperty(backend.getAbility(i).get());
            bindObservableInteger(abilities[i], backend.getAbility(i));
        }

        abilityModifiers = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilityModifiers[i] = new SimpleIntegerProperty(backend.getAbilityModifier(i).get());
            bindObservableInteger(abilityModifiers[i], backend.getAbilityModifier(i));
        }

        savingThrowModifiers = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            savingThrowModifiers[i] = new SimpleIntegerProperty(backend.getSavingThrowModifier(i).get());
            bindObservableInteger(savingThrowModifiers[i], backend.getSavingThrowModifier(i));
        }

        skillModifiers = new IntegerProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            skillModifiers[i] = new SimpleIntegerProperty(backend.getSkillModifier(i).get());
            bindObservableInteger(skillModifiers[i], backend.getSkillModifier(i));
        }

        abilityBases = new IntegerProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilityBases[i] = new SimpleIntegerProperty(backend.getAbilityBase(i).get());
            bindObservableInteger(abilityBases[i], backend.getAbilityBase(i));
        }

        availablePlusOnes = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            availablePlusOnes[i] = new SimpleBooleanProperty(backend.getAvailablePlusOne(i).get());
            bindObservableBoolean(availablePlusOnes[i], backend.getAvailablePlusOne(i));
        }

        availablePlusTwos = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            availablePlusTwos[i] = new SimpleBooleanProperty(backend.getAvailablePlusTwo(i).get());
            bindObservableBoolean(availablePlusTwos[i], backend.getAvailablePlusTwo(i));
        }

        availablePluses = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            availablePluses[i] = new SimpleBooleanProperty(backend.getAvailablePlus(i).get());
            bindObservableBoolean(availablePluses[i], backend.getAvailablePlus(i));
        }

        availableMinuses = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            availableMinuses[i] = new SimpleBooleanProperty(backend.getAvailableMinus(i).get());
            bindObservableBoolean(availableMinuses[i], backend.getAvailableMinus(i));
        }

        availableSkills = new BooleanProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            availableSkills[i] = new SimpleBooleanProperty(backend.getAvailableSkill(i).get());
            bindObservableBoolean(availableSkills[i], backend.getAvailableSkill(i));
        }

        abilityPlusOnes = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilityPlusOnes[i] = new SimpleBooleanProperty(backend.getAbilityPlusOne(i).get());
            bindObservableBoolean(abilityPlusOnes[i], backend.getAbilityPlusOne(i));
        }

        abilityPlusTwos = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilityPlusTwos[i] = new SimpleBooleanProperty(backend.getAbilityPlusTwo(i).get());
            bindObservableBoolean(abilityPlusTwos[i], backend.getAbilityPlusTwo(i));
        }

        savingThrowProficiencies = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            savingThrowProficiencies[i] = new SimpleBooleanProperty(backend.getSavingThrowProficiency(i).get());
            bindObservableBoolean(savingThrowProficiencies[i], backend.getSavingThrowProficiency(i));
        }

        skillProficiencies = new BooleanProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            skillProficiencies[i] = new SimpleBooleanProperty(backend.getSkillProficiency(i).get());
            bindObservableBoolean(skillProficiencies[i], backend.getSkillProficiency(i));
        }

        traits = FXCollections.observableArrayList();
        updateList(traits, backend.getTraits());

        weaponProficiencies = FXCollections.observableArrayList();
        updateList(weaponProficiencies, backend.getWeaponProficiencies());

        armorProficiencies = FXCollections.observableArrayList();
        updateList(armorProficiencies, backend.getArmorProficiencies());

        toolProficiencies = FXCollections.observableArrayList();
        updateList(toolProficiencies, backend.getToolProficiencies());

        choiceToolProficiencies = FXCollections.observableArrayList();
        updateCustomList(choiceToolProficiencies, backend.getChoiceToolProficiencies());

        selectableFeats = FXCollections.observableArrayList();
        availableCantrips = FXCollections.observableArrayList();
        availableSpells = FXCollections.observableArrayList();
        for (int i = 0; i < maxClasses; i++) {
            ObservableList<StringProperty> selectableFeat = FXCollections.observableArrayList();
            selectableFeats.add(selectableFeat);
            updateList(selectableFeat, backend.getNewSelectableFeats(i));

            ObservableList<Spell> classCantrips = FXCollections.observableArrayList();
            availableCantrips.add(classCantrips);
            updateCustomListNoEdits(classCantrips, backend.getAvailableCantrips(i));

            ObservableList<Spell> classSpells = FXCollections.observableArrayList();
            availableSpells.add(classSpells);
            updateCustomListNoEdits(classSpells, backend.getAvailableSpells(i));
        }

        cantrips = FXCollections.observableArrayList();
        updateCustomListNoEdits(cantrips, backend.getCantrips());

        spells = FXCollections.observableArrayList();
        updateCustomListNoEdits(spells, backend.getSpells());

        items = FXCollections.observableArrayList();
        updateCustomListNoEdits(items, backend.getItems());

        maxFeats = backend.getMaxFeats();
        availableFeats = new IntegerProperty[maxClasses];
        feats = new StringProperty[maxClasses][maxFeats];
        featOnes = new StringProperty[maxClasses][maxFeats];
        featTwos = new StringProperty[maxClasses][maxFeats];
        featAbilities = new StringProperty[maxClasses][maxFeats][abilityCount];
        for (int i = 0; i < maxClasses; i++) {
            availableFeats[i] = new SimpleIntegerProperty(backend.getAvailableFeats(i).get());
            bindObservableInteger(availableFeats[i], backend.getAvailableFeats(i));

            for (int j = 0; j < maxFeats; j++) {
                feats[i][j] = new SimpleStringProperty(getTranslation(backend.getFeat(i, j).get()));
                bindObservableString(feats[i][j], backend.getFeat(i, j));

                featOnes[i][j] = new SimpleStringProperty(getTranslation(backend.getFeatOne(i, j).get()));
                bindObservableString(featOnes[i][j], backend.getFeatOne(i, j));

                featTwos[i][j] = new SimpleStringProperty(getTranslation(backend.getFeatTwo(i, j).get()));
                bindObservableString(featTwos[i][j], backend.getFeatTwo(i, j));

                for (int k = 0; k < abilityCount; k++) {
                    featAbilities[i][j][k] = new SimpleStringProperty(getTranslation(backend.getFeatAbility(i, j, k).get()));
                    bindObservableString(featAbilities[i][j][k], backend.getFeatAbility(i, j, k));
                }
            }
        }
    }

    private <T extends MyItems<T>> void updateCustomListNoEdits(ObservableList<T> front, CustomObservableList<T> back) {
        AtomicBoolean updating = new AtomicBoolean(false);

        java.util.function.Consumer<ObservableList<T>> updateFtB = (ObservableList<T> newValue) -> {
            characterTab.newEdit();
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<T> original = new java.util.ArrayList<>();
                for (T key : newValue) {
                    T copy = (T) key.copy();
                    copy.setName(getOriginal(copy.getName()));
                    original.add(copy);
                }
                back.setAll(original);
            } finally {
                updating.set(false);
            }
        };

        front.addListener((ListChangeListener<T>) _ -> updateFtB.accept(front));
        
        java.util.function.Consumer<CustomObservableList<T>> updateBtF = (CustomObservableList<T> newValue) -> {
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<T> translated = new java.util.ArrayList<>();
                for (T key : newValue.getList()) {
                    T copy = (T) key.copy();
                    copy.setName(getTranslation(copy.getName()));
                    translated.add(copy);
                }
                front.setAll(translated);
            } finally {
                updating.set(false);
            }
        };

        back.addListener(newVal -> updateBtF.accept(newVal));
        updateBtF.accept(back);
    }

    private <T extends MyItems<T>> void updateCustomList(ObservableList<T> front, CustomObservableList<T> back) {
        AtomicBoolean updating = new AtomicBoolean(false);

        java.util.function.Consumer<ObservableList<T>> updateFtB = (ObservableList<T> newValue) -> {
            characterTab.newEdit();
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<T> original = new java.util.ArrayList<>();
                for (T key : newValue) {
                    T copy = (T) key.copy();
                    copy.setName(getOriginal(copy.getName()));
                    original.add(copy);
                    copy.getNameProperty().addListener(newVal -> {
                        key.setName(getTranslation(newVal));
                        // System.out.println("Updated front name to: " + key.getName());
                    });
                    key.getNameProperty().addListener(newVal -> {
                        characterTab.newEdit();
                        copy.setName(getOriginal(newVal));
                    });
                }
                back.setAll(original);
            } finally {
                updating.set(false);
            }
        };

        front.addListener((ListChangeListener<T>) _ -> updateFtB.accept(front));
        
        java.util.function.Consumer<CustomObservableList<T>> updateBtF = (CustomObservableList<T> newValue) -> {
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<T> translated = new java.util.ArrayList<>();
                for (T key : newValue.getList()) {
                    T copy = (T) key.copy();
                    copy.setName(getTranslation(copy.getName()));
                    translated.add(copy);
                    copy.getNameProperty().addListener(newVal -> {
                        characterTab.newEdit();
                        key.setName(getOriginal(newVal));
                        // System.out.println("Updated back name to: " + key.getName());
                    });
                    key.getNameProperty().addListener(newVal -> {
                        copy.setName(getTranslation(newVal));
                    });
                }
                front.setAll(translated);
            } finally {
                updating.set(false);
            }
        };

        back.addListener(newVal -> updateBtF.accept(newVal));
        updateBtF.accept(back);
        updateFtB.accept(front);
    }

    private void updateList(ObservableList<StringProperty> front, CustomObservableList<ObservableString> back) {
        AtomicBoolean updating = new AtomicBoolean(false);
        
        Runnable updateFtB = () -> {
            characterTab.newEdit();
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<ObservableString> original = new java.util.ArrayList<>();
                for (StringProperty key : front) {
                    original.add(new ObservableString(getOriginal(key.get())));
                }
                back.setAll(original);
            } finally {
                updating.set(false);
            }
        };

        front.addListener((ListChangeListener<StringProperty>) _ -> updateFtB.run());
        
        Runnable updateBtF = () -> {
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<StringProperty> translated = new java.util.ArrayList<>();
                for (ObservableString key : back.asList()) {
                    translated.add(new SimpleStringProperty(getTranslation(key.get())));
                }
                front.setAll(translated);
            } finally {
                updating.set(false);
            }
        };

        back.addListener(_ -> updateBtF.run());
        updateBtF.run();
    }

    private void bindObservableItem(ObservableItem front, ObservableItem back) {
        back.addListener(_ -> {
            front.set(back.get().copy());
            front.get().setName(getTranslation(back.get().getName()));
        });
        front.addListener(_ -> {
            characterTab.newEdit();
            back.set(front.get().copy());
        });
    }

    private void bindObservableString(StringProperty front, ObservableString back) {
        back.addListener(_ -> {
            // System.out.println("Back changed: " + back.get() + " -> Front: " + getTranslation(back.get()));
            front.set(getTranslation(back.get()));
        });
        front.addListener(_ -> {
            characterTab.newEdit();
            // System.out.println("Front changed: " + front.get() + " -> Back: " + getOriginal(front.get()));
            back.set(getOriginal(front.get()));
        });
    }

    private void bindObservableInteger(IntegerProperty front, ObservableInteger back) {
        back.addListener(_ -> front.set(back.get()));
        front.addListener(_ -> {
            characterTab.newEdit();
            back.set(front.get());
        });
    }

    private void bindObservableDouble(DoubleProperty front, ObservableInteger back, double multiplier) {
        back.addListener(_ -> front.set(back.get() * multiplier));
        front.addListener(_ -> {
            characterTab.newEdit();
            back.set((int) (front.get() / multiplier));
        });
    }

    private void bindObservableBoolean(BooleanProperty front, ObservableBoolean back) {
        back.addListener(_ -> front.set(back.get()));
        front.addListener(_ -> {
            characterTab.newEdit();
            back.set(front.get());
        });
    }

    // Editors

    public void AbilityBasePlus(int index) {
        abilityBases[index].set(abilityBases[index].get() + 1);
    }

    public void AbilityBaseMinus(int index) {
        abilityBases[index].set(abilityBases[index].get() - 1);
    }

    // Getters for all properties

    public int[] getSkillAbilities() {
        return skillAbilities;
    }

    public ObservableItem getMainHand() {
        return mainHand;
    }

    public ObservableItem getOffHand() {
        return offHand;
    }

    public ObservableItem getArmor() {
        return armor;
    }

    public ObservableItem getShield() {
        return shield;
    }

    public ObservableList<Spell> getCantrips() {
        return cantrips;
    }

    public ObservableList<Spell> getSpells() {
        return spells;
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public ObservableList<StringProperty> getTraits() {
        return traits;
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

    public ObservableList<StringProperty> getSelectableFeats(int index) {
        return selectableFeats.get(index);
    }

    public StringProperty getClassEquipment(int index) {
        return classEquipment[index];
    }

    public StringProperty[] getClassEquipments() {
        return classEquipment;
    }

    public StringProperty getBackgroundEquipment(int index) {
        return backgroundEquipment[index];
    }

    public Proficiency getChoiceToolProficiency(int index) {
        return choiceToolProficiencies.get(index);
    }

    public ObservableList<ObservableList<Spell>> getAvailableCantrips() {
        return availableCantrips;
    }

    public ObservableList<ObservableList<Spell>> getAvailableSpells() {
        return availableSpells;
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

    public String getSaveName() {
        return saveName.get();
    }

    public StringProperty getUserDescription() {
        return userDescription;
    }

    public StringProperty getFinesseAbility() {
        return finesseAbility;
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

    public StringProperty getSubclass(int index) {
        return subclasses[index];
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

    public StringProperty getLevelShown(int index) {
        return levelsShown[index];
    }

    public StringProperty getClasse(int index) {
        return classes[index];
    }
    public StringProperty[] getClasses() {
        return classes;
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

    public StringProperty[] getSpellcastingAbilities() {
        return spellcastingAbilities;
    }

    public StringProperty getSpellcastingAbility(int index) {
        return spellcastingAbilities[index];
    }

    public StringProperty getFeat(int classIndex, int index) {
        return feats[classIndex][index];
    }

    public StringProperty getFeatOne(int classIndex, int index) {
        return featOnes[classIndex][index];
    }

    public StringProperty getFeatTwo(int classIndex, int index) {
        return featTwos[classIndex][index];
    }

    public StringProperty[] getFeatAbilities(int classIndex, int index) {
        return featAbilities[classIndex][index];
    }

    public StringProperty[] getAvailableSizes() {
        return availableSizes;
    }


    public StringProperty[] getAvailableSubclasses(int index) {
        return availableSubclasses[index];
    }

    public StringProperty[] getAvailableLineages() {
        return availableLineages;
    }


    public int getMaxFeats() {
        return maxFeats;
    }
    public int getMaxClasses() {
        return maxClasses;
    }

    public IntegerProperty getSpellcastingAbilityModifier(int index) {
        return spellcastingAbilityModifiers[index];
    }

    public IntegerProperty getSpellcastingAttackModifier(int index) {
        return spellcastingAttackModifiers[index];
    }

    public IntegerProperty getSpellcastingSaveDC(int index) {
        return spellcastingSaveDCs[index];
    }

    public IntegerProperty getSpellSlot(int index) {
        return spellSlots[index];
    }

    public IntegerProperty getAvailableSpellSlot(int index) {
        return availableSpellSlots[index];
    }

    public IntegerProperty getMaxCantrips(int index) {
        return maxCantrips[index];
    }

    public IntegerProperty getMaxSpells(int index) {
        return maxSpells[index];
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

    public IntegerProperty getTotalLevel() {
        return totalLevel;
    }

    public IntegerProperty getLevel(int index) {
        return levels[index];
    }

    public IntegerProperty getAvailableFeats(int index) {
        return availableFeats[index];
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

    public IntegerProperty[] getHitDies() {
        return hitDies;
    }

    public IntegerProperty getHitDie(int index) {
        return hitDies[index];
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


    public BooleanProperty hasShieldProficiency() {
        return hasShieldProficiency;
    }

    public BooleanProperty hasArmorProficiency() {
        return hasArmorProficiency;
    }

    public BooleanProperty hasMainProficiency() {
        return hasMainProficiency;
    }

    public BooleanProperty hasOffProficiency() {
        return hasOffProficiency;
    }

    public BooleanProperty isGenerator() {
        return isGenerator;
    }

    public BooleanProperty isEditing() {
        return isEditing;
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
        return TranslationManager.getTranslation(key);
    }

    private String getOriginal(String key) {
        return TranslationManager.getOriginal(key);
    }


    public ViewModel duplicate() {
        return new ViewModel(backend.duplicate(), stage, characterTab);
    }

    public void fill(boolean firstTime) {
        backend.fill(firstTime);
    }

    public Boolean save(Boolean newFile) {
        Boolean saveSuccessful = backend.save(newFile, stage);
        if (saveSuccessful) {
            characterTab.setSaved();
        }
        return saveSuccessful;
    }

    public ViewModel load() {
        GameCharacter character = GameCharacter.load(stage);
        if (character != null) {
            return new ViewModel(character, stage, characterTab);
        } else {
            return null;
        }
    }

    public GameCharacter getBackend() {
        return backend;
    }

    public void addItem(String itemName) {
        backend.addItem(itemName);
    }

    public void setCharacterTab(CharacterTab characterTab) {
        this.characterTab = characterTab;
    }
}