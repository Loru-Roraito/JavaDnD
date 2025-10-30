package com.dnd;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dnd.characters.GameCharacter;
import com.dnd.items.MyItems;
import com.dnd.utils.Constants;
import com.dnd.utils.CustomObservableList;
import com.dnd.utils.ObservableBoolean;
import com.dnd.utils.ObservableInteger;
import com.dnd.utils.ObservableString;
import com.dnd.items.Proficiency;
import com.dnd.items.Spell;

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
    private final StringProperty spellcastingAbility;
    private final StringProperty[] moneysShown;
    private final StringProperty[] feats;
    private final StringProperty[] featOnes;
    private final StringProperty[] featTwos;
    private final StringProperty[] availableSizes;
    private final StringProperty[] availableSubclasses;
    private final StringProperty[] availableLineages;
    private final StringProperty[] abilityBasesShown;
    private final StringProperty[] classEquipment;
    private final StringProperty[] backgroundEquipment;
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
    private final IntegerProperty maxCantrips;
    private final IntegerProperty maxSpells;
    private final IntegerProperty spellcastingAbilityModifier;
    private final IntegerProperty spellcastingAttackModifier;
    private final IntegerProperty spellcastingSaveDC;
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
    private final ObservableList<StringProperty> selectableFeats;
    private final ObservableList<Proficiency> choiceProficiencies;
    private final ObservableList<Spell> availableCantrips;
    private final ObservableList<Spell> availableSpells;
    private final ObservableList<Spell> spells;
    private final ObservableList<Spell> cantrips;

    private final GameCharacter backend;

    public ViewModel(GameCharacter backend) {
        this.backend = backend;
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

        subclass = new SimpleStringProperty(getTranslation(backend.getSubclass().get()));
        bindObservableString(subclass, backend.getSubclass());

        lineage = new SimpleStringProperty(getTranslation(backend.getLineage().get()));
        bindObservableString(lineage, backend.getLineage());

        alignment = new SimpleStringProperty(getTranslation(backend.getAlignment().get()));
        bindObservableString(alignment, backend.getAlignment());

        generationMethod = new SimpleStringProperty(getTranslation(backend.getGenerationMethod().get()));
        bindObservableString(generationMethod, backend.getGenerationMethod());

        healthMethod = new SimpleStringProperty(getTranslation(backend.getHealthMethod().get()));
        bindObservableString(healthMethod, backend.getHealthMethod());

        levelShown = new SimpleStringProperty(getTranslation(backend.getLevelShown().get()));
        bindObservableString(levelShown, backend.getLevelShown());

        classe = new SimpleStringProperty(getTranslation(backend.getClasse().get()));
        bindObservableString(classe, backend.getClasse());

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

        hitDie = new SimpleIntegerProperty(backend.getHitDie().get());
        bindObservableInteger(hitDie, backend.getHitDie());

        level = new SimpleIntegerProperty(backend.getLevel().get());
        bindObservableInteger(level, backend.getLevel());

        exhaustion = new SimpleIntegerProperty(backend.getExhaustion().get());
        bindObservableInteger(exhaustion, backend.getExhaustion());

        maxCantrips = new SimpleIntegerProperty(backend.getMaxCantrips().get());
        bindObservableInteger(maxCantrips, backend.getMaxCantrips());

        maxSpells = new SimpleIntegerProperty(backend.getMaxSpells().get());
        bindObservableInteger(maxSpells, backend.getMaxSpells());

        spellcastingAbilityModifier = new SimpleIntegerProperty(backend.getSpellcastingAbilityModifier().get());
        bindObservableInteger(spellcastingAbilityModifier, backend.getSpellcastingAbilityModifier());

        spellcastingAttackModifier = new SimpleIntegerProperty(backend.getSpellcastingAttackModifier().get());
        bindObservableInteger(spellcastingAttackModifier, backend.getSpellcastingAttackModifier());

        spellcastingSaveDC = new SimpleIntegerProperty(backend.getSpellcastingSaveDC().get());
        bindObservableInteger(spellcastingSaveDC, backend.getSpellcastingSaveDC());

        size = new SimpleStringProperty(getTranslation(backend.getSize().get()));
        bindObservableString(size, backend.getSize());

        originFeat = new SimpleStringProperty(getTranslation(backend.getOriginFeat().get()));
        bindObservableString(originFeat, backend.getOriginFeat());

        spellcastingAbility = new SimpleStringProperty(getTranslation(backend.getSpellcastingAbility().get()));
        bindObservableString(spellcastingAbility, backend.getSpellcastingAbility());

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

        availableSubclasses = new StringProperty[maxSubclasses];
        for (int i = 0; i < maxSubclasses; i++) {
            availableSubclasses[i] = new SimpleStringProperty(getTranslation(backend.getAvailableSubclass(i).get()));
            bindObservableString(availableSubclasses[i], backend.getAvailableSubclass(i));
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

        actives = FXCollections.observableArrayList();
        updateList(actives, backend.getActives());

        passives = FXCollections.observableArrayList();
        updateList(passives, backend.getPassives());

        weaponProficiencies = FXCollections.observableArrayList();
        updateList(weaponProficiencies, backend.getWeaponProficiencies());

        armorProficiencies = FXCollections.observableArrayList();
        updateList(armorProficiencies, backend.getArmorProficiencies());

        toolProficiencies = FXCollections.observableArrayList();
        updateList(toolProficiencies, backend.getToolProficiencies());

        selectableFeats = FXCollections.observableArrayList();
        updateList(selectableFeats, backend.getNewSelectableFeats());

        choiceProficiencies = FXCollections.observableArrayList();
        updateCustomList(choiceProficiencies, backend.getChoiceProficiencies());

        availableCantrips = FXCollections.observableArrayList();
        updateCustomList(availableCantrips, backend.getAvailableCantrips());

        availableSpells = FXCollections.observableArrayList();
        updateCustomList(availableSpells, backend.getAvailableSpells());

        cantrips = FXCollections.observableArrayList();
        updateCustomList(cantrips, backend.getCantrips());

        spells = FXCollections.observableArrayList();
        updateCustomList(spells, backend.getSpells());

        maxFeats = backend.getMaxFeats();
        availableFeats = new SimpleIntegerProperty(backend.getAvailableFeats().get());
        bindObservableInteger(availableFeats, backend.getAvailableFeats());
        feats = new StringProperty[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            feats[i] = new SimpleStringProperty(getTranslation(backend.getFeat(i).get()));
            bindObservableString(feats[i], backend.getFeat(i));
        }

        featAbilities = new StringProperty[maxFeats][abilityCount];
        for (int i = 0; i < maxFeats; i++) {
            for (int j = 0; j < abilityCount; j++) {
                featAbilities[i][j] = new SimpleStringProperty(getTranslation(backend.getFeatAbility(i, j).get()));
                bindObservableString(featAbilities[i][j], backend.getFeatAbility(i, j));
            }
        }

        featOnes = new StringProperty[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            featOnes[i] = new SimpleStringProperty(getTranslation(backend.getFeatOne(i).get()));
            bindObservableString(featOnes[i], backend.getFeatOne(i));
        }

        featTwos = new StringProperty[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            featTwos[i] = new SimpleStringProperty(getTranslation(backend.getFeatTwo(i).get()));
            bindObservableString(featTwos[i], backend.getFeatTwo(i));
        }
    }

    private <T extends MyItems<T>> void updateCustomList(ObservableList<T> front, CustomObservableList<T> back) {
        AtomicBoolean updating = new AtomicBoolean(false);

        java.util.function.Consumer<ObservableList<T>> updateFtB = (ObservableList<T> newValue) -> {
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

    private void bindObservableString(StringProperty front, ObservableString back) {
        back.addListener(_ -> {
            // System.out.println("Back changed: " + back.get() + " -> Front: " + getTranslation(back.get()));
            front.set(getTranslation(back.get()));
        });
        front.addListener(_ -> {
            // System.out.println("Front changed: " + front.get() + " -> Back: " + getOriginal(front.get()));
            back.set(getOriginal(front.get()));
        });
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

    public ObservableList<Spell> getCantrips() {
        return cantrips;
    }

    public ObservableList<Spell> getSpells() {
        return spells;
    }

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

    public StringProperty getClassEquipment(int index) {
        return classEquipment[index];
    }

    public StringProperty[] getClassEquipments() {
        return classEquipment;
    }

    public StringProperty getBackgroundEquipment(int index) {
        return backgroundEquipment[index];
    }

    public Proficiency getChoiceProficiency(int index) {
        return choiceProficiencies.get(index);
    }

    public ObservableList<Spell> getAvailableCantrips() {
        return availableCantrips;
    }

    public ObservableList<Spell> getAvailableSpells() {
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

    public StringProperty getSpellcastingAbility() {
        return spellcastingAbility;
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

    public IntegerProperty getSpellcastingAbilityModifier() {
        return spellcastingAbilityModifier;
    }

    public IntegerProperty getSpellcastingAttackModifier() {
        return spellcastingAttackModifier;
    }

    public IntegerProperty getSpellcastingSaveDC() {
        return spellcastingSaveDC;
    }

    public IntegerProperty getSpellSlot(int index) {
        return spellSlots[index];
    }

    public IntegerProperty getAvailableSpellSlot(int index) {
        return availableSpellSlots[index];
    }

    public IntegerProperty getMaxCantrips() {
        return maxCantrips;
    }

    public IntegerProperty getMaxSpells() {
        return maxSpells;
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


    public ViewModel duplicate() {
        return new ViewModel(backend.duplicate());
    }

    public void fill() {
        backend.fill();
    }
}