package com.dnd.frontend;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dnd.backend.GameCharacter;
import com.dnd.frontend.language.Constants;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.tabs.CharacterTab;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.MyItems;
import com.dnd.utils.items.Proficiency;
import com.dnd.utils.items.Spell;
import com.dnd.utils.observables.CustomObservableList;
import com.dnd.utils.observables.ObservableBoolean;
import com.dnd.utils.observables.ObservableInteger;
import com.dnd.utils.observables.ObservableItem;
import com.dnd.utils.observables.ObservableString;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private final StringProperty currentHealthShown;
    private final StringProperty[] classes;
    private final StringProperty[] subclasses;
    private final StringProperty[] levelsShown;
    private final StringProperty[] spellcastingAbilities;
    private final StringProperty[] moneysShown;
    private final StringProperty[] selectableSizes;
    private final StringProperty[] selectableLineages;
    private final StringProperty[] abilityBasesShown;
    private final StringProperty[] classEquipment;
    private final StringProperty[] backgroundEquipment;
    private final StringProperty[][] selectableSubclasses;
    private final StringProperty[][] feats;
    private final StringProperty[][] featOnes;
    private final StringProperty[][] featTwos;
    private final StringProperty[][][] featAbilities;

    private final int maxFeats;
    private final int maxClasses;
    private final int[] skillAbilities;
    
    // Maybe unnecessary? Int or Float could work? Right now I'll leave it like this, but is probably unoptimal (probably negligible, though).
    private final DoubleProperty speed;
    private final DoubleProperty darkvision;

    private final BooleanProperty isGenerator = new SimpleBooleanProperty(true);
    private final BooleanProperty isEditing = new SimpleBooleanProperty(true);
    private final BooleanProperty isShortResting = new SimpleBooleanProperty(false);
    private final BooleanProperty isLongResting = new SimpleBooleanProperty(false);
    private final BooleanProperty isLevelingUp = new SimpleBooleanProperty(false);
    private final BooleanProperty[] areLevelingUp;

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
    
    private final ObservableList<String> selectableLanguages;
    private final ObservableList<String> selectableAbilities; 
    private final ObservableList<String> selectableClasses;
    private final ObservableList<String> traits;
    private final ObservableList<String> weaponProficiencies;
    private final ObservableList<String> armorProficiencies;
    private final ObservableList<String> toolProficiencies;
    private final ObservableList<String> totalToolProficiencies;
    private final ObservableList<ObservableList<String>> selectableFeats;
    private final ObservableList<Proficiency> choiceToolProficiencies;
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

        bindObservableInteger(backend.getTotalLevel());

        species = new SimpleStringProperty(getTranslation(backend.getSpecies().get()));
        bindObservableString(species, backend.getSpecies());

        background = new SimpleStringProperty(getTranslation(backend.getBackground().get()));
        bindObservableString(background, backend.getBackground());

        bindObservableInteger(backend.getGenerationPoints());

        bindObservableInteger(backend.getInitiativeBonus());

        bindObservableInteger(backend.getProficiencyBonus());

        speed = new SimpleDoubleProperty(backend.getSpeed().get() * Constants.LENGTH_MULTIPLIER);
        bindObservableDouble(speed, backend.getSpeed(), Constants.LENGTH_MULTIPLIER);

        darkvision = new SimpleDoubleProperty(backend.getDarkvision().get() * Constants.LENGTH_MULTIPLIER);
        bindObservableDouble(darkvision, backend.getDarkvision(), Constants.LENGTH_MULTIPLIER);

        bindObservableInteger(backend.getArmorClass());

        bindObservableInteger(backend.getHealth());
        bindObservableInteger(backend.getCurrentHealth());

        bindObservableInteger(backend.getFixedHealth());

        bindObservableInteger(backend.getExhaustion());

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

        bindObservableBoolean(backend.hasShieldProficiency());

        bindObservableBoolean(backend.hasArmorProficiency());

        bindObservableBoolean(backend.hasMainProficiency());

        bindObservableBoolean(backend.hasOffProficiency());

        currentHealthShown = new SimpleStringProperty(getTranslation(backend.getCurrentHealthShown().get()));
        bindObservableString(currentHealthShown, backend.getCurrentHealthShown());

        maxClasses = backend.getMaxClasses();
        levelsShown = new SimpleStringProperty[maxClasses];
        classes = new SimpleStringProperty[maxClasses];
        subclasses = new SimpleStringProperty[maxClasses];
        spellcastingAbilities = new SimpleStringProperty[maxClasses];
        areLevelingUp = new BooleanProperty[maxClasses];

        for (int i = 0; i < maxClasses; i++) {
            areLevelingUp[i] = new SimpleBooleanProperty(false);

            levelsShown[i] = new SimpleStringProperty(getTranslation(backend.getLevelShown(i).get()));
            bindObservableString(levelsShown[i], backend.getLevelShown(i));

            classes[i] = new SimpleStringProperty(getTranslation(backend.getClasse(i).get()));
            bindObservableString(classes[i], backend.getClasse(i));

            subclasses[i] = new SimpleStringProperty(getTranslation(backend.getSubclass(i).get()));
            bindObservableString(subclasses[i], backend.getSubclass(i));

            bindObservableInteger(backend.getHitDie(i));

            bindObservableInteger(backend.getLevel(i));

            bindObservableInteger(backend.getMaxCantrips(i));

            bindObservableInteger(backend.getMaxSpells(i));

            bindObservableInteger(backend.getSpellcastingAbilityModifier(i));

            bindObservableInteger(backend.getSpellcastingAttackModifier(i));

            bindObservableInteger(backend.getSpellcastingSaveDC(i));

            spellcastingAbilities[i] = new SimpleStringProperty(getTranslation(backend.getSpellcastingAbility(i).get()));
            bindObservableString(spellcastingAbilities[i], backend.getSpellcastingAbility(i));
        }

        for (int i = 0; i < 4; i ++) {
            bindObservableInteger(backend.getAvailableHitDie(i));
            bindObservableInteger(backend.getMaximumHitDie(i));
        }

        moneysShown = new SimpleStringProperty[5];
        for (int i = 0; i < 5; i++) {
            moneysShown[i] = new SimpleStringProperty(getTranslation(backend.getMoneyShown(i).get()));
            bindObservableString(moneysShown[i], backend.getMoneyShown(i));
        }

        selectableSizes = new StringProperty[2];
        for (int i = 0; i < 2; i++) {
            selectableSizes[i] = new SimpleStringProperty(getTranslation(backend.getAvailableSize(i).get()));
            bindObservableString(selectableSizes[i], backend.getAvailableSize(i));
        }

        int maxSubclasses = backend.getMaxSubclasses();
        int maxLineages = backend.getMaxLineages();
        int maxSets = backend.getMaxSets();

        selectableSubclasses = new StringProperty[maxClasses][maxSubclasses];
        for (int i = 0; i < maxClasses; i++) {
            for (int j = 0; j < maxSubclasses; j++) {
                selectableSubclasses[i][j] = new SimpleStringProperty(getTranslation(backend.getSelectableSubclass(i, j).get()));
                bindObservableString(selectableSubclasses[i][j], backend.getSelectableSubclass(i, j));
            }
        }

        selectableLineages = new StringProperty[maxLineages];
        for (int i = 0; i < maxLineages; i++) {
            selectableLineages[i] = new SimpleStringProperty(getTranslation(backend.getSelectableLineage(i).get()));
            bindObservableString(selectableLineages[i], backend.getSelectableLineage(i));
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

        for (int i = 0; i < 9; i ++) {
            bindObservableInteger(backend.getSpellSlot(i));
            bindObservableInteger(backend.getSelectableSpellslot(i));
        }

        int skillCount = backend.getSkillNames().length;
        int abilityCount = backend.getAbilityNames().length;

        abilityBasesShown = new StringProperty[abilityCount];
        availablePlusOnes = new BooleanProperty[abilityCount];
        availablePlusTwos = new BooleanProperty[abilityCount];
        availablePluses = new BooleanProperty[abilityCount];
        availableMinuses = new BooleanProperty[abilityCount];
        abilityPlusOnes = new BooleanProperty[abilityCount];
        abilityPlusTwos = new BooleanProperty[abilityCount];
        savingThrowProficiencies = new BooleanProperty[abilityCount];
        for (int i = 0; i < abilityCount; i++) {
            abilityBasesShown[i] = new SimpleStringProperty(getTranslation(backend.getAbilityBasesShown(i).get()));
            bindObservableString(abilityBasesShown[i], backend.getAbilityBasesShown(i));
            
            bindObservableInteger(backend.getAbility(i));
            
            bindObservableInteger(backend.getAbilityModifier(i));
            
            bindObservableInteger(backend.getSavingThrowModifier(i));

            bindObservableInteger(backend.getAbilityBase(i));
            
            availablePlusOnes[i] = new SimpleBooleanProperty(backend.getAvailablePlusOne(i).get());
            bindObservableBoolean(availablePlusOnes[i], backend.getAvailablePlusOne(i));
            
            availablePlusTwos[i] = new SimpleBooleanProperty(backend.getAvailablePlusTwo(i).get());
            bindObservableBoolean(availablePlusTwos[i], backend.getAvailablePlusTwo(i));
            
            availablePluses[i] = new SimpleBooleanProperty(backend.getAvailablePlus(i).get());
            bindObservableBoolean(availablePluses[i], backend.getAvailablePlus(i));

            availableMinuses[i] = new SimpleBooleanProperty(backend.getAvailableMinus(i).get());
            bindObservableBoolean(availableMinuses[i], backend.getAvailableMinus(i));
            
            abilityPlusOnes[i] = new SimpleBooleanProperty(backend.getAbilityPlusOne(i).get());
            bindObservableBoolean(abilityPlusOnes[i], backend.getAbilityPlusOne(i));

            abilityPlusTwos[i] = new SimpleBooleanProperty(backend.getAbilityPlusTwo(i).get());
            bindObservableBoolean(abilityPlusTwos[i], backend.getAbilityPlusTwo(i));
            
            savingThrowProficiencies[i] = new SimpleBooleanProperty(backend.getSavingThrowProficiency(i).get());
            bindObservableBoolean(savingThrowProficiencies[i], backend.getSavingThrowProficiency(i));
        }

        availableSkills = new BooleanProperty[skillCount];
        skillProficiencies = new BooleanProperty[skillCount];
        for (int i = 0; i < skillCount; i++) {
            bindObservableInteger(backend.getSkillModifier(i));
            
            availableSkills[i] = new SimpleBooleanProperty(backend.getAvailableSkill(i).get());
            bindObservableBoolean(availableSkills[i], backend.getAvailableSkill(i));
            
            skillProficiencies[i] = new SimpleBooleanProperty(backend.getSkillProficiency(i).get());
            bindObservableBoolean(skillProficiencies[i], backend.getSkillProficiency(i));
        }

        selectableLanguages = FXCollections.observableArrayList();
        updateList(selectableLanguages, backend.getSelectableLanguages());

        selectableAbilities = FXCollections.observableArrayList();
        updateList(selectableAbilities, backend.getSelectableAbilities());

        selectableClasses = FXCollections.observableArrayList();
        updateList(selectableClasses, backend.getSelectableClasses());

        traits = FXCollections.observableArrayList();
        updateList(traits, backend.getTraits());

        weaponProficiencies = FXCollections.observableArrayList();
        updateList(weaponProficiencies, backend.getWeaponProficiencies());

        armorProficiencies = FXCollections.observableArrayList();
        updateList(armorProficiencies, backend.getArmorProficiencies());

        toolProficiencies = FXCollections.observableArrayList();
        updateList(toolProficiencies, backend.getToolProficiencies());

        totalToolProficiencies = FXCollections.observableArrayList();
        updateList(totalToolProficiencies, backend.getTotalToolProficiencies());

        choiceToolProficiencies = FXCollections.observableArrayList();
        updateCustomList(choiceToolProficiencies, backend.getChoiceToolProficiencies());

        selectableFeats = FXCollections.observableArrayList();
        for (int i = 0; i < maxClasses; i++) {
            ObservableList<String> selectableFeat = FXCollections.observableArrayList();
            selectableFeats.add(selectableFeat);
            updateList(selectableFeat, backend.getNewSelectableFeats(i));

            updateCustomListNoEdits(backend.getSelectableCantrips().getList().get(i));
            updateCustomListNoEdits(backend.getSelectableSpells().getList().get(i));
        }

        for (int i = 0; i < maxClasses; i++) {
            updateCustomListNoEdits(backend.getCantrips().getList().get(i));
            updateCustomListNoEdits(backend.getSpells().getList().get(i));
        }

        items = FXCollections.observableArrayList();
        updateCustomListNoEdits(items, backend.getItems());

        maxFeats = backend.getMaxFeats();
        feats = new StringProperty[maxClasses][maxFeats];
        featOnes = new StringProperty[maxClasses][maxFeats];
        featTwos = new StringProperty[maxClasses][maxFeats];
        featAbilities = new StringProperty[maxClasses][maxFeats][abilityCount];
        for (int i = 0; i < maxClasses; i++) {
            bindObservableInteger(backend.getAvailableFeats(i));

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

    private <T extends MyItems<T>> void updateCustomListNoEdits(CustomObservableList<T> list) {
        list.addListener(_ -> {
            characterTab.newEdit();
            for (T item : list.getList()) {
                item.setName(getTranslation(item.getName()));
            }
        });
        for (T item : list.getList()) {
            item.setName(getTranslation(item.getName()));
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

    private void updateList(ObservableList<String> front, CustomObservableList<String> back) {
        AtomicBoolean updating = new AtomicBoolean(false);
        
        Runnable updateFtB = () -> {
            characterTab.newEdit();
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<String> original = new java.util.ArrayList<>();
                for (String key : front) {
                    original.add(getOriginal(key));
                }
                back.setAll(original);
            } finally {
                updating.set(false);
            }
        };

        front.addListener((ListChangeListener<String>) _ -> updateFtB.run());
        
        Runnable updateBtF = () -> {
            if (!updating.compareAndSet(false, true)) return;
            try {
                List<String> translated = new java.util.ArrayList<>();
                for (String key : back.asList()) {
                    translated.add(getTranslation(key));
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

    private void bindObservableInteger(ObservableInteger integer) {
        integer.addListener(_ -> {
            characterTab.newEdit();
        });
    }

    private void bindObservableDouble(DoubleProperty front, ObservableInteger back, double multiplier) {
        back.addListener(_ -> front.set(back.get() * multiplier));
        front.addListener(_ -> {
            characterTab.newEdit();
            back.set((int) (front.get() / multiplier));
        });
    }

    private void bindObservableBoolean(ObservableBoolean bool) {
        bool.addListener(_ -> {
            characterTab.newEdit();
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
        backend.getAbilityBase(index).set(backend.getAbilityBase(index).get() + 1);
    }

    public void AbilityBaseMinus(int index) {
        backend.getAbilityBase(index).set(backend.getAbilityBase(index).get() - 1);
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

    public CustomObservableList<Spell> getCantrips(int index) {
        return backend.getCantrips().getList().get(index);
    }

    public CustomObservableList<Spell> getSpells(int index) {
        return backend.getSpells().getList().get(index);
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public ObservableList<String> getTraits() {
        return traits;
    }

    public ObservableList<String> getWeaponProficiencies() {
        return weaponProficiencies;
    }

    public ObservableList<String> getArmorProficiencies() {
        return armorProficiencies;
    }

    public ObservableList<String> getToolProficiencies() {
        return toolProficiencies;
    }

    public ObservableList<String> getTotalToolProficiencies() {
        return totalToolProficiencies;
    }

    public ObservableList<String> getSelectableFeats(int index) {
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

    public ObservableList<String> getSelectableLanguages() {
        return selectableLanguages;
    }

    public ObservableList<String> getSelectableAbilities() {
        return selectableAbilities;
    }

    public CustomObservableList<CustomObservableList<Spell>> getSelectableCantrips() {
        return backend.getSelectableCantrips();
    }

    public CustomObservableList<CustomObservableList<Spell>> getSelectableSpells() {
        return backend.getSelectableSpells();
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

    public StringProperty getCurrentHealthShown() {
        return currentHealthShown;
    }

    public StringProperty getClasse(int index) {
        return classes[index];
    }

    public StringProperty[] getClasses() {
        return classes;
    }

    public ObservableList<String> getselectableClasses() {
        return selectableClasses;
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

    public StringProperty[] getSelectableSizes() {
        return selectableSizes;
    }


    public StringProperty[] getSelectableSubclasses(int index) {
        return selectableSubclasses[index];
    }

    public StringProperty[] getSelectableLineages() {
        return selectableLineages;
    }

    public int getMaxFeats() {
        return maxFeats;
    }
    
    public int getMaxClasses() {
        return maxClasses;
    }

    public ObservableInteger getSpellcastingAbilityModifier(int index) {
        return backend.getSpellcastingAbilityModifier(index);
    }

    public ObservableInteger getSpellcastingAttackModifier(int index) {
        return backend.getSpellcastingAttackModifier(index);
    }

    public ObservableInteger getSpellcastingSaveDC(int index) {
        return backend.getSpellcastingSaveDC(index);
    }

    public ObservableInteger getSpellSlot(int index) {
        return backend.getSpellSlot(index);
    }

    public ObservableInteger getSelectableSpellslot(int index) {
        return backend.getSelectableSpellslot(index);
    }

    public ObservableInteger getMaxCantrips(int index) {
        return backend.getMaxCantrips(index);
    }

    public ObservableInteger getMaxSpells(int index) {
        return backend.getMaxSpells(index);
    }

    public ObservableInteger getExhaustion() {
        return backend.getExhaustion();
    }

    public ObservableInteger getGenerationPoints() {
        return backend.getGenerationPoints();
    }

    public ObservableInteger getInitiativeBonus() {
        return backend.getInitiativeBonus();
    }

    public ObservableInteger getProficiencyBonus() {
        return backend.getProficiencyBonus();
    }

    public DoubleProperty getSpeed() {
        return speed;
    }

    public DoubleProperty getDarkvision() {
        return darkvision;
    }

    public ObservableInteger getTotalLevel() {
        return backend.getTotalLevel();
    }

    public ObservableInteger getLevel(int index) {
        return backend.getLevel(index);
    }

    public ObservableInteger getAvailableFeats(int index) {
        return backend.getAvailableFeats(index);
    }

    public ObservableInteger getArmorClass() {
        return backend.getArmorClass();
    }

    public ObservableInteger getHealth() {
        return backend.getHealth();
    }

    public ObservableInteger getCurrentHealth() {
        return backend.getCurrentHealth();
    }

    public ObservableInteger getFixedHealth() {
        return backend.getFixedHealth();
    }

    public ObservableInteger getHitDie(int index) {
        return backend.getHitDie(index);
    }

    public ObservableInteger[] getHitDies() {
        return backend.getHitDies();
    }

    public ObservableInteger getAvailableHitDie(int index) {
        return backend.getAvailableHitDie(index);
    }

    public ObservableInteger[] getAvailableHitDies() {
        return backend.getAvailableHitDies();
    }

    public ObservableInteger getMaximumHitDie(int index) {
        return backend.getMaximumHitDie(index);
    }
    
    public ObservableInteger[] getMaximumHitDies() {
        return backend.getMaximumHitDies();
    }

    public StringProperty getMoneyShown(int index) {
        return moneysShown[index];
    }

    public ObservableInteger getAbilityBase(int index) {
        return backend.getAbilityBase(index);
    }

    public ObservableInteger getAbility(int index) {
        return backend.getAbility(index);
    }


    public ObservableInteger getAbilityModifier(int index) {
        return backend.getAbilityModifier(index);
    }

    public ObservableInteger getSavingThrowModifier(int index) {
        return backend.getSavingThrowModifier(index);
    }

    public ObservableInteger getSkillModifier(int index) {
        return backend.getSkillModifier(index);
    }


    public ObservableBoolean hasShieldProficiency() {
        return backend.hasShieldProficiency();
    }

    public ObservableBoolean hasArmorProficiency() {
        return backend.hasArmorProficiency();
    }

    public ObservableBoolean hasMainProficiency() {
        return backend.hasMainProficiency();
    }

    public ObservableBoolean hasOffProficiency() {
        return backend.hasOffProficiency();
    }

    public BooleanProperty isGenerator() {
        return isGenerator;
    }

    public BooleanProperty isEditing() {
        return isEditing;
    }

    public BooleanProperty isShortResting() {
        return isShortResting;
    }

    public BooleanProperty isLongResting() {
        return isLongResting;
    }

    public BooleanProperty isLevelingUp() {
        return isLevelingUp;
    }

    public BooleanProperty areLevelingUp(int index) {
        return areLevelingUp[index];
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