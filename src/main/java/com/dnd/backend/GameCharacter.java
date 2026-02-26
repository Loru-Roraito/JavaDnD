package com.dnd.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.utils.ThrowManager;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.Proficiency;
import com.dnd.utils.items.Spell;
import com.dnd.utils.observables.CustomObservableList;
import com.dnd.utils.observables.ObservableBoolean;
import com.dnd.utils.observables.ObservableInteger;
import com.dnd.utils.observables.ObservableItem;
import com.dnd.utils.observables.ObservableString;
import javafx.stage.Stage;

/*
    How to add a field to GameCharacter:
     - Declare it here
     - Share it with ViewModel
     - If independent, it must be insertend in GameCharacter.fill() and in CharacterSerializer
 */
public class GameCharacter {
    private final ObservableItem mainHand = new ObservableItem(new Item("UNARMED_STRIKE"));
    private final ObservableItem offHand = new ObservableItem(new Item("NONE"));
    private final ObservableItem armor = new ObservableItem(new Item("NONE"));
    private final ObservableItem shield = new ObservableItem(new Item("NONE"));

    private String path = "";
    private static String[] skillNames; // Get the names of all skills
    private static String[] abilityNames;
    private final String[] sets;

    private final ObservableString userDescription = new ObservableString("");
    private final ObservableString finesseAbility = new ObservableString("BEST_ONE");
    private final ObservableString saveName = new ObservableString("");
    private final ObservableString creatureType = new ObservableString("");
    private final ObservableString languageOne = new ObservableString("RANDOM");
    private final ObservableString languageTwo = new ObservableString("RANDOM");
    private final ObservableString height = new ObservableString("");
    private final ObservableString weight = new ObservableString("");
    private final ObservableString name = new ObservableString("");
    private final ObservableString gender = new ObservableString("RANDOM");
    private final ObservableString lineage = new ObservableString("RANDOM");
    private final ObservableString alignment = new ObservableString("RANDOM");
    private final ObservableString generationMethod = new ObservableString("STANDARD_ARRAY");
    private final ObservableString healthMethod = new ObservableString("MEDIUM_HP");
    private final ObservableString species = new ObservableString("RANDOM");
    private final ObservableString background = new ObservableString("RANDOM");
    private final ObservableString size = new ObservableString("");
    private final ObservableString originFeat = new ObservableString("");
    private final ObservableString currentHealthShown = new ObservableString("1");
    private final ObservableString[] spellcastingAbilities;
    private final ObservableString[] moneysShown = new ObservableString[5];
    private final ObservableString[] classes;
    private final ObservableString[] subclasses;
    private final ObservableString[] levelsShown;
    private final ObservableString[] selectableSizes;
    private final ObservableString[] selectableLineages;
    private final ObservableString[] abilityBasesShown;
    private final ObservableString[] classEquipment;
    private final ObservableString[] backgroundEquipment;
    private final ObservableString[][] feats;
    private final ObservableString[][] featOnes;
    private final ObservableString[][] featTwos;
    private final ObservableString[][] selectableSubclasses;
    private final ObservableString[][][] featsAbilities;

    private final CustomObservableList<Proficiency> choiceToolProficiencies = new CustomObservableList<>();
    private final CustomObservableList<String> selectableAbilities = new CustomObservableList<>();
    private final CustomObservableList<String> selectableClasses;
    private final CustomObservableList<String> traits = new CustomObservableList<>();
    private final CustomObservableList<String> weaponProficiencies = new CustomObservableList<>();
    private final CustomObservableList<String> armorProficiencies = new CustomObservableList<>();
    private final CustomObservableList<String> toolProficiencies = new CustomObservableList<>();
    private final CustomObservableList<String> selectableLanguages = new CustomObservableList<>();
    private final CustomObservableList<CustomObservableList<String>> possibleFeats = new CustomObservableList<>();
    private final CustomObservableList<CustomObservableList<String>> selectableFeats = new CustomObservableList<>();
    private final CustomObservableList<CustomObservableList<Spell>> selectableCantrips = new CustomObservableList<>();
    private final CustomObservableList<CustomObservableList<Spell>> selectableSpells = new CustomObservableList<>();
    private final CustomObservableList<CustomObservableList<Spell>> spells = new CustomObservableList<>();
    private final CustomObservableList<CustomObservableList<Spell>> cantrips = new CustomObservableList<>();
    private final CustomObservableList<Item> items = new CustomObservableList<>();

    // TODO: change those arrays into lists
    private static int maxSubclasses;
    private static int maxLineages;
    private static int maxFeats;
    private static int maxSets;
    private static int maxClasses;

    private int maxSkills;
    private static int[] standardArray = new int[] { 15, 14, 13, 12, 10, 8 };
    private static int[] skillAbilities;

    private final ObservableInteger totalLevel = new ObservableInteger(0);
    private final ObservableInteger initiativeBonus = new ObservableInteger(0);
    private final ObservableInteger proficiencyBonus = new ObservableInteger(2);
    private final ObservableInteger speed = new ObservableInteger(30);
    private final ObservableInteger darkvision = new ObservableInteger(60);
    private final ObservableInteger armorClass = new ObservableInteger(10);
    private final ObservableInteger health = new ObservableInteger(1);
    private final ObservableInteger currentHealth = new ObservableInteger(1);
    private final ObservableInteger fixedHealth = new ObservableInteger(0);
    private final ObservableInteger givenBonuses = new ObservableInteger(0);
    private final ObservableInteger givenSkills = new ObservableInteger(0);
    private final ObservableInteger generationPoints = new ObservableInteger(0);
    private final ObservableInteger exhaustion = new ObservableInteger(0);
    private final ObservableInteger[] spellcastingAbilityModifiers;
    private final ObservableInteger[] spellcastingAttackModifiers;
    private final ObservableInteger[] spellcastingSaveDCs;
    private final ObservableInteger[] maxSpells;
    private final ObservableInteger[] maxCantrips;
    private final ObservableInteger[] availableFeats;
    private final ObservableInteger[] hitDies;
    private final ObservableInteger[] levels;
    private final ObservableInteger[] abilityBases;
    private final ObservableInteger[] abilities;
    private final ObservableInteger[] abilityModifiers;
    private final ObservableInteger[] savingThrowBonuses;
    private final ObservableInteger[] savingThrowModifiers;
    private final ObservableInteger[] skillBonuses;
    private final ObservableInteger[] skillModifiers;
    private final ObservableInteger[] moneys = new ObservableInteger[5];
    private final ObservableInteger[] spellSlots = new ObservableInteger[9];
    private final ObservableInteger[] availableSpellSlots = new ObservableInteger[9];
    private final ObservableInteger[] maximumHitDies = new ObservableInteger[4];
    private final ObservableInteger[] availableHitDies = new ObservableInteger[4];

    private final ObservableBoolean blinded = new ObservableBoolean(false);
    private final ObservableBoolean charmed = new ObservableBoolean(false);
    private final ObservableBoolean deafened = new ObservableBoolean(false);
    private final ObservableBoolean frightened = new ObservableBoolean(false);
    private final ObservableBoolean grappled = new ObservableBoolean(false);
    private final ObservableBoolean incapacitated = new ObservableBoolean(false);
    private final ObservableBoolean incapacitation = new ObservableBoolean(false);
    private final ObservableBoolean invisible = new ObservableBoolean(false);
    private final ObservableBoolean paralyzed = new ObservableBoolean(false);
    private final ObservableBoolean petrified = new ObservableBoolean(false);
    private final ObservableBoolean poisoned = new ObservableBoolean(false);
    private final ObservableBoolean prone = new ObservableBoolean(false);
    private final ObservableBoolean proneness = new ObservableBoolean(false);
    private final ObservableBoolean restrained = new ObservableBoolean(false);
    private final ObservableBoolean stunned = new ObservableBoolean(false);
    private final ObservableBoolean unconscious = new ObservableBoolean(false);
    private final ObservableBoolean hasShieldProficiency = new ObservableBoolean(false);
    private final ObservableBoolean hasArmorProficiency = new ObservableBoolean(false);
    private final ObservableBoolean hasMainProficiency = new ObservableBoolean(false);
    private final ObservableBoolean hasOffProficiency = new ObservableBoolean(false);

    private final ObservableBoolean[] availableSkills;
    private final ObservableBoolean[] abilityPlusOnes;
    private final ObservableBoolean[] abilityPlusTwos;
    private final ObservableBoolean[] fixedSkills;
    private final ObservableBoolean[] savingThrowProficiencies;
    private final ObservableBoolean[] availablePluses;
    private final ObservableBoolean[] availableMinuses;
    private final ObservableBoolean[] availablePlusOnes;
    private final ObservableBoolean[] availablePlusTwos;
    private final ObservableBoolean[] skillProficiencies;

    public GameCharacter() {
        skillNames = getStrings(new String[] { "skills" });
        abilityNames = getStrings(new String[] { "abilities" });
        sets = getStrings(new String[] { "sets" });
        int skillCount = skillNames.length;
        int abilityCount = abilityNames.length;

        // Initialize with default values
        skillAbilities = new int[skillCount];
        abilityBases = new ObservableInteger[abilityCount];
        abilities = new ObservableInteger[abilityCount];
        abilityModifiers = new ObservableInteger[abilityCount];
        savingThrowBonuses = new ObservableInteger[abilityCount];
        savingThrowModifiers = new ObservableInteger[abilityCount];
        skillBonuses = new ObservableInteger[skillCount];
        skillModifiers = new ObservableInteger[skillCount];

        abilityPlusOnes = new ObservableBoolean[abilityCount];
        abilityPlusTwos = new ObservableBoolean[abilityCount];
        savingThrowProficiencies = new ObservableBoolean[abilityCount];
        skillProficiencies = new ObservableBoolean[skillCount];

        abilityBasesShown = new ObservableString[abilityCount];

        availableSkills = new ObservableBoolean[skillCount];
        fixedSkills = new ObservableBoolean[skillCount];

        maxClasses = getInt(new String[] { "max_classes" });
        classes = new ObservableString[maxClasses];
        subclasses = new ObservableString[maxClasses];
        levelsShown = new ObservableString[maxClasses];
        levels = new ObservableInteger[maxClasses];
        hitDies = new ObservableInteger[maxClasses];
        availableFeats = new ObservableInteger[maxClasses];
        maxSpells = new ObservableInteger[maxClasses];
        maxCantrips = new ObservableInteger[maxClasses];
        spellcastingAbilities = new ObservableString[maxClasses];
        spellcastingAbilityModifiers = new ObservableInteger[maxClasses];
        spellcastingAttackModifiers = new ObservableInteger[maxClasses];
        spellcastingSaveDCs = new ObservableInteger[maxClasses];

        for (int i = 0; i < maxClasses; i++) {
            spells.add(new CustomObservableList<>());
            cantrips.add(new CustomObservableList<>());
        }

        classes[0] = new ObservableString("RANDOM");
        subclasses[0] = new ObservableString("RANDOM");
        levelsShown[0] = new ObservableString("RANDOM");
        levels[0] = new ObservableInteger(0);
        hitDies[0] = new ObservableInteger(0);
        availableFeats[0] = new ObservableInteger(0);
        maxSpells[0] = new ObservableInteger(0);
        maxCantrips[0] = new ObservableInteger(0);
        spellcastingAbilities[0] = new ObservableString("");
        spellcastingAbilityModifiers[0] = new ObservableInteger(0);
        spellcastingAttackModifiers[0] = new ObservableInteger(0);
        spellcastingSaveDCs[0] = new ObservableInteger(0);

        for (int i = 1; i < maxClasses; i++) {
            classes[i] = new ObservableString("NONE");
            subclasses[i] = new ObservableString("RANDOM");
            levelsShown[i] = new ObservableString("RANDOM");
            levels[i] = new ObservableInteger(0);
            hitDies[i] = new ObservableInteger(0);
            availableFeats[i] = new ObservableInteger(0);
            maxSpells[i] = new ObservableInteger(0);
            maxCantrips[i] = new ObservableInteger(0);
            spellcastingAbilities[i] = new ObservableString("NONE");
            spellcastingAbilityModifiers[i] = new ObservableInteger(0);
            spellcastingAttackModifiers[i] = new ObservableInteger(0);
            spellcastingSaveDCs[i] = new ObservableInteger(0);
        }

        for (int i = 0; i < maximumHitDies.length; i++) {
            maximumHitDies[i] = new ObservableInteger(0);
            availableHitDies[i] = new ObservableInteger(0);
        }

        bindLevel();
        bindProficiencyBonus();

        bindLanguages();

        availablePlusOnes = new ObservableBoolean[abilityCount];
        availablePlusTwos = new ObservableBoolean[abilityCount];
        availablePluses = new ObservableBoolean[abilityCount];
        availableMinuses = new ObservableBoolean[abilityCount];

        for (int i = 0; i < abilityBases.length; i++) {
            // Initialize each ability with a default value of 10
            abilityBases[i] = new ObservableInteger(0);
            abilities[i] = new ObservableInteger(10);
            abilityModifiers[i] = new ObservableInteger(0);

            savingThrowBonuses[i] = new ObservableInteger(0);
            savingThrowModifiers[i] = new ObservableInteger(0);

            abilityBasesShown[i] = new ObservableString("RANDOM");

            abilityPlusOnes[i] = new ObservableBoolean(false);
            abilityPlusTwos[i] = new ObservableBoolean(false);

            availablePlusOnes[i] = new ObservableBoolean(false);
            availablePlusTwos[i] = new ObservableBoolean(false);

            availablePluses[i] = new ObservableBoolean(false);
            availableMinuses[i] = new ObservableBoolean(false);

            savingThrowProficiencies[i] = new ObservableBoolean(false);
        }

        for (int i = 0; i < abilityBases.length; i++) {
            bindAbilityBase(i);

            bindAvailablePlusOne(i);
            bindAvailablePlusTwo(i);

            bindAvailablePluses(i);
            bindAvailableMinuses(i);
        }

        maxFeats = getInt(new String[] { "max_feats" });
        feats = new ObservableString[maxClasses][maxFeats];
        featOnes = new ObservableString[maxClasses][maxFeats];
        featTwos = new ObservableString[maxClasses][maxFeats];
        featsAbilities = new ObservableString[maxClasses][maxFeats][abilityCount];
        for (int i = 0; i < maxClasses; i++) {
            for (int j = 0; j < maxFeats; j++) {
                feats[i][j] = new ObservableString("RANDOM");
                featOnes[i][j] = new ObservableString("NONE");
                featTwos[i][j] = new ObservableString("NONE");
                for (int k = 0; k < abilityCount; k++) {
                    featsAbilities[i][j][k] = new ObservableString("");
                }
            }
        }

        bindFeatsAbilities();

        maxSets = getInt(new String[] { "max_sets" });
        classEquipment = new ObservableString[maxSets + 1];
        backgroundEquipment = new ObservableString[maxSets + 1];

        for (int i = 0; i < maxSets + 1; i++) {
            classEquipment[i] = new ObservableString("RANDOM");
            backgroundEquipment[i] = new ObservableString("RANDOM");
        }

        for (int i = 0; i < abilityBases.length; i++) {
            bindFinalAbility(i);
            bindAbilityModifier(i);
        }

        bindGivenBonuses();

        bindFixedSkills();

        maxSubclasses = getInt(new String[] { "max_subclasses" });
        selectableSubclasses = new ObservableString[maxClasses][maxSubclasses];
        bindselectableSubclasses();

        selectableClasses = new CustomObservableList<>();
        bindselectableClasses();

        for (int i = 0; i < abilityBases.length; i++) {
            bindSavingThrowProficiencies(i);

            bindSavingThrowBonus(i);
            bindSavingThrowModifier(i);
        }

        for (int i = 0; i < skillBonuses.length; i++) {
            skillAbilities[i] = getInt(new String[] { "skills", skillNames[i], "ability" });
            skillBonuses[i] = new ObservableInteger(0);
            skillModifiers[i] = new ObservableInteger(0);
            skillProficiencies[i] = new ObservableBoolean(false);
            availableSkills[i] = new ObservableBoolean(false);

            bindSkillProficiency(i);
            bindSkillBonus(i);
            bindSkillModifier(i);
            bindAvailableSkills(i);
        }

        bindGivenSkills();

        maxLineages = getInt(new String[] { "max_lineages" });
        selectableLineages = new ObservableString[maxLineages];
        bindselectableLineages();

        bindGenerationPoints();
        bindGenerationMethod();

        bindSpeed();
        bindDarkvision();
        bindArmorClass();
        bindInitiativeBonus();

        bindHitDie();
        bindHealth();
        bindCreatureType();

        selectableSizes = new ObservableString[2];
        bindselectableSizes();

        bindAvailableFeats();
        bindFeatOnesTwos();

        bindOriginFeat();

        bindTraits();

        bindChoiceToolProficiencies();

        bindWeaponProficiencies();
        bindArmorProficiencies();
        bindToolProficiencies();

        bindClassEquipment();
        bindBackgroundEquipment();

        bindSelectableFeats();
        bindIncapacitated();
        bindProne();

        for (int i = 0; i < 5; i++) {
            moneys[i] = new ObservableInteger(0);
            moneysShown[i] = new ObservableString("0");
        }

        bindMoneys();

        for (int i = 0; i < 9; i++) {
            spellSlots[i] = new ObservableInteger(0);
            availableSpellSlots[i] = new ObservableInteger(0);
        }

        bindSpells();
        bindMaxSpells();
        bindSpellcastingAbility();

        bindSubclass();

        bindHasProficiencies();
    }

    // Getters
    public String[] getSkillNames() {
        return skillNames;
    }

    public String[] getAbilityNames() {
        return abilityNames;
    }

    public ObservableString getAvailableSize(int index) {
        return selectableSizes[index];
    }

    public ObservableString getSize() {
        return size;
    }

    public ObservableString getCreatureType() {
        return creatureType;
    }

    public ObservableString getName() {
        return name;
    }

    public ObservableString getLanguageOne() {
        return languageOne;
    }

    public ObservableString getLanguageTwo() {
        return languageTwo;
    }

    public ObservableString getHeight() {
        return height;
    }

    public ObservableString getWeight() {
        return weight;
    }

    public ObservableString getGender() {
        return gender;
    }

    public ObservableString getSubclass(int index) {
        return subclasses[index];
    }

    public ObservableString getLineage() {
        return lineage;
    }

    public ObservableString getAlignment() {
        return alignment;
    }

    public ObservableString getGenerationMethod() {
        return generationMethod;
    }

    public ObservableString getHealthMethod() {
        return healthMethod;
    }

    public ObservableString getSpellcastingAbility(int index) {
        return spellcastingAbilities[index];
    }

    public ObservableInteger getSpellcastingAbilityModifier(int index) {
        return spellcastingAbilityModifiers[index];
    }

    public ObservableInteger getSpellcastingAttackModifier(int index) {
        return spellcastingAttackModifiers[index];
    }

    public ObservableInteger getSpellcastingSaveDC(int index) {
        return spellcastingSaveDCs[index];
    }

    public ObservableString getOriginFeat() {
        return originFeat;
    }

    public ObservableString getLevelShown(int index) {
        return levelsShown[index];
    }

    public ObservableString getCurrentHealthShown() {
        return currentHealthShown;
    }

    public ObservableInteger getLevel(int index) {
        return levels[index];
    }

    public ObservableInteger getTotalLevel() {
        return totalLevel;
    }

    public ObservableString getClasse(int index) {
        return classes[index];
    }

    public ObservableString getSpecies() {
        return species;
    }

    public ObservableString getBackground() {
        return background;
    }

    public CustomObservableList<String> getSelectableLanguages() {
        return selectableLanguages;
    }

    public CustomObservableList<String> getSelectableClasses() {
        return selectableClasses;
    }

    public ObservableString getSelectableSubclass(int classIndex, int subclassIndex) {
        return selectableSubclasses[classIndex][subclassIndex];
    }

    public ObservableString getSelectableLineage(int index) {
        return selectableLineages[index];
    }

    public ObservableString getAbilityBasesShown(int index) {
        return abilityBasesShown[index];
    }

    public ObservableString getFeat(int classIndex, int featIndex) {
        return feats[classIndex][featIndex];
    }

    public ObservableString getFeatOne(int classIndex, int index) {
        return featOnes[classIndex][index];
    }

    public ObservableString getFeatTwo(int classIndex, int index) {
        return featTwos[classIndex][index];
    }

    public ObservableString getFeatAbility(int classIndex, int featIndex, int abilityIndex) {
        return featsAbilities[classIndex][featIndex][abilityIndex];
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ObservableString getUserDescription() {
        return userDescription;
    }

    public ObservableString getFinesseAbility() {
        return finesseAbility;
    }

    public String getSaveName() {
        return saveName.get();
    }

    public void setSaveName(String saveName) {
        this.saveName.set(saveName);
    }

    public ObservableString getSaveNameProperty() {
        return saveName;
    }

    public int getMaxLineages() {
        return maxLineages;
    }

    public int getMaxSubclasses() {
        return maxSubclasses;
    }

    public int getMaxSets() {
        return maxSets;
    }

    public int getMaxFeats() {
        return maxFeats;
    }

    public int getMaxClasses() {
        return maxClasses;
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

    public ObservableInteger getAvailableFeats(int index) {
        return availableFeats[index];
    }

    public ObservableInteger getMoney(int index) {
        return moneys[index];
    }

    public ObservableString getMoneyShown(int index) {
        return moneysShown[index];
    }

    public ObservableInteger getAbilityBase(int index) {
        return abilityBases[index];
    }

    public ObservableInteger getExhaustion() {
        return exhaustion;
    }

    public ObservableInteger getGenerationPoints() {
        return generationPoints;
    }

    public ObservableInteger getInitiativeBonus() {
        return initiativeBonus;
    }

    public ObservableInteger getProficiencyBonus() {
        return proficiencyBonus;
    }

    public ObservableInteger getSpeed() {
        return speed;
    }

    public ObservableInteger getDarkvision() {
        return darkvision;
    }

    public ObservableInteger getArmorClass() {
        return armorClass;
    }

    public ObservableInteger getHealth() {
        return health;
    }

    public ObservableInteger getCurrentHealth() {
        return currentHealth;
    }

    public ObservableInteger getFixedHealth() {
        return fixedHealth;
    }

    public ObservableInteger getHitDie(int index) {
        return hitDies[index];
    }

    public ObservableInteger[] getHitDies() {
        return hitDies;
    }

    public ObservableInteger getAvailableHitDie(int index) {
        return availableHitDies[index];
    }

    public ObservableInteger[] getAvailableHitDies() {
        return availableHitDies;
    }

    public ObservableInteger getMaximumHitDie(int index) {
        return maximumHitDies[index];
    }

    public ObservableInteger[] getMaximumHitDies() {
        return maximumHitDies;
    }

    public ObservableInteger getAbility(int index) {
        return abilities[index];
    }

    public ObservableInteger getAbilityModifier(int index) {
        return abilityModifiers[index];
    }

    public ObservableInteger getSavingThrowModifier(int index) {
        return savingThrowModifiers[index];
    }

    public ObservableInteger getSkillModifier(int index) {
        return skillModifiers[index];
    }

    public ObservableInteger getSpellSlot(int index) {
        return spellSlots[index];
    }

    public ObservableInteger getSelectableSpellslot(int index) {
        return availableSpellSlots[index];
    }

    public ObservableInteger getMaxCantrips(int index) {
        return maxCantrips[index];
    }

    public ObservableInteger getMaxSpells(int index) {
        return maxSpells[index];
    }

    public ObservableBoolean getBlinded() {
        return blinded;
    }

    public ObservableBoolean getCharmed() {
        return charmed;
    }

    public ObservableBoolean getDeafened() {
        return deafened;
    }

    public ObservableBoolean getFrightened() {
        return frightened;
    }

    public ObservableBoolean getGrappled() {
        return grappled;
    }

    public ObservableBoolean getIncapacitated() {
        return incapacitated;
    }

    public ObservableBoolean getIncapacitation() {
        return incapacitation;
    }

    public ObservableBoolean getInvisible() {
        return invisible;
    }

    public ObservableBoolean getParalyzed() {
        return paralyzed;
    }

    public ObservableBoolean getPetrified() {
        return petrified;
    }

    public ObservableBoolean getPoisoned() {
        return poisoned;
    }

    public ObservableBoolean getProne() {
        return prone;
    }

    public ObservableBoolean getProneness() {
        return proneness;
    }

    public ObservableBoolean getRestrained() {
        return restrained;
    }

    public ObservableBoolean getStunned() {
        return stunned;
    }

    public ObservableBoolean getUnconscious() {
        return unconscious;
    }

    public ObservableBoolean getAvailablePlusOne(int index) {
        return availablePlusOnes[index];
    }

    public ObservableBoolean getAvailablePlusTwo(int index) {
        return availablePlusTwos[index];
    }

    public ObservableBoolean getAvailablePlus(int index) {
        return availablePluses[index];
    }

    public ObservableBoolean getAvailableMinus(int index) {
        return availableMinuses[index];
    }

    public ObservableBoolean getAvailableSkill(int index) {
        return availableSkills[index];
    }

    public ObservableBoolean getAbilityPlusOne(int index) {
        return abilityPlusOnes[index];
    }

    public ObservableBoolean getAbilityPlusTwo(int index) {
        return abilityPlusTwos[index];
    }

    public ObservableBoolean getSavingThrowProficiency(int index) {
        return savingThrowProficiencies[index];
    }

    public ObservableBoolean getSkillProficiency(int index) {
        return skillProficiencies[index];
    }

    public ObservableBoolean hasShieldProficiency() {
        return hasShieldProficiency;
    }

    public ObservableBoolean hasArmorProficiency() {
        return hasArmorProficiency;
    }

    public ObservableBoolean hasMainProficiency() {
        return hasMainProficiency;
    }

    public ObservableBoolean hasOffProficiency() {
        return hasOffProficiency;
    }

    public int[] getSkillAbilities() {
        return skillAbilities;
    }

    public CustomObservableList<String> getTraits() {
        return traits;
    }

    public CustomObservableList<String> getWeaponProficiencies() {
        return weaponProficiencies;
    }

    public CustomObservableList<String> getArmorProficiencies() {
        return armorProficiencies;
    }

    public CustomObservableList<String> getToolProficiencies() {
        return toolProficiencies;
    }

    public ObservableString getClassEquipment(int index) {
        return classEquipment[index];
    }

    public ObservableString getBackgroundEquipment(int index) {
        return backgroundEquipment[index];
    }

    public CustomObservableList<Proficiency> getChoiceToolProficiencies() {
        return choiceToolProficiencies;
    }

    public CustomObservableList<String> getSelectableAbilities() {
        return selectableAbilities;
    }

    public CustomObservableList<String> getNewSelectableFeats(int index) {
        return selectableFeats.getList().get(index);
    }

    public CustomObservableList<CustomObservableList<Spell>> getSelectableCantrips() {
        return selectableCantrips;
    }

    public CustomObservableList<CustomObservableList<Spell>> getSelectableSpells() {
        return selectableSpells;
    }

    public CustomObservableList<CustomObservableList<Spell>> getSpells() {
        return spells;
    }

    public CustomObservableList<CustomObservableList<Spell>> getCantrips() {
        return cantrips;
    }

    public CustomObservableList<Item> getItems() {
        return items;
    }

    // Binders
    private void bindselectableSubclasses() {
        // Listen for changes to the 'classe' property and update selectableSubclasses
        // accordingly

        for (int classIndex = 0; classIndex < maxClasses; classIndex++) {
            int index = classIndex;
            Runnable updateSubclasses = () -> {
                if (levels[index].get() >= 3) {
                    String[] possibleSubclasses = getStrings(
                            new String[] { "classes", classes[index].get(), "subclasses" });
                    for (int i = 0; i < selectableSubclasses[index].length; i++) {
                        if (possibleSubclasses != null && i < possibleSubclasses.length) {
                            selectableSubclasses[index][i].set(possibleSubclasses[i]);
                        } else {
                            selectableSubclasses[index][i].set("");
                        }
                    }
                } else {
                    // If level < 3, clear all subclasses
                    for (ObservableString availableSubclass : selectableSubclasses[index]) {
                        availableSubclass.set("");
                    }
                }
            };

            classes[index].addListener(_ -> updateSubclasses.run());
            levels[index].addListener(_ -> updateSubclasses.run());

            for (int i = 0; i < selectableSubclasses[index].length; i++) {
                selectableSubclasses[index][i] = new ObservableString("");
            }

            // Initial population
            updateSubclasses.run();
        }
    }

    private void bindselectableClasses() {
        Runnable updateselectableClasses = () -> {
            String[] totalClasses = getStrings(new String[] { "classes" });
            List<String> remainingClasses = new ArrayList<>();
            remainingClasses.add("RANDOM");
            for (String totalClass : totalClasses) {
                remainingClasses.add(totalClass);
            }

            for (ObservableString classe : classes) {
                if (!classe.get().equals("RANDOM")) {
                    remainingClasses.removeIf(c -> c.equals(classe.get()));
                }
            }

            selectableClasses.setAll(remainingClasses);
        };
        for (ObservableString classe : classes) {
            classe.addListener(_ -> updateselectableClasses.run());
        }

        updateselectableClasses.run();
    }

    private void bindOriginFeat() {
        background.addListener(
            (newVal) -> {
                originFeat.set(getString(new String[] { "backgrounds", newVal, "feat" }));
                backgroundEquipment[0].set(newVal);
            });
    }

    private void bindselectableSizes() {
        species.addListener(
                (newVal) -> {
                    String[] sizes = getStrings(new String[] { "species", newVal, "size" });
                    for (int i = 0; i < selectableSizes.length; i++) {
                        if (sizes != null && i < sizes.length) {
                            selectableSizes[i].set(sizes[i]);
                        } else {
                            selectableSizes[i].set("");
                        }
                    }
                    if (selectableSizes[1].get().equals("")) {
                        size.set(sizes != null && sizes.length > 0 ? sizes[0] : "");
                    } else if (sizes != null && !Arrays.asList(sizes).contains(size.get())) {
                        size.set("RANDOM");
                    }
                });

        // Initialize selectableSizes with empty strings
        for (int i = 0; i < selectableSizes.length; i++) {
            selectableSizes[i] = new ObservableString("");
        }
    }

    private void bindAvailableFeats() {
        for (int classIndex = 0; classIndex < feats.length; classIndex++) {
            int index = classIndex;
            Runnable updateAvailableFeats = () -> {
                int[] newFeats = getInts(new String[] { "classes", classes[index].get(), "feats" });
                int i = 0;
                if (newFeats != null) {
                    for (int possibleFeat : newFeats) {
                        if (possibleFeat <= levels[index].get()) {
                            i++;
                        } else {
                            availableFeats[index].set(i);
                            return;
                        }
                    }
                }
                availableFeats[index].set(i);
            };

            classes[index].addListener(_ -> updateAvailableFeats.run());
            levels[index].addListener(_ -> updateAvailableFeats.run());

            availableFeats[index].addListener(
                    (newVal) -> {
                        for (int i = newVal; i < feats[index].length; i++) {
                            feats[index][i].set("RANDOM");
                        }
                    });
        }
    }

    private void bindFeatOnesTwos() {
        for (int i = 0; i < classes.length; i++) {
            int classIndex = i;
            for (int j = 0; j < feats[classIndex].length; j++) {
                int index = j;
                feats[classIndex][index].addListener(
                        (newVal) -> {
                            String[] abilitiesPossible = getStrings(new String[] { "feats", newVal, "abilities" });
                            String randomValue = "RANDOM";
                            if (abilitiesPossible.length == 1) {
                                randomValue = abilitiesPossible[0];
                            } else if (abilitiesPossible.length == 0) {
                                randomValue = "NONE";
                            }
                            int n = getInt(new String[] { "feats", newVal, "max" });
                            switch (n) {
                                case 2 -> {
                                    featTwos[classIndex][index].set(randomValue);
                                    featOnes[classIndex][index].set(randomValue);
                                }
                                case 1 -> {
                                    featTwos[classIndex][index].set("NONE");
                                    featOnes[classIndex][index].set(randomValue);
                                }
                                default -> {
                                    featTwos[classIndex][index].set("NONE");
                                    featOnes[classIndex][index].set("NONE");
                                }
                            }
                        });
            }
        }
    }

    private void bindselectableLineages() {
        species.addListener(
                (newVal) -> {
                    String[] lineages = getStrings(new String[] { "species", newVal, "lineages" });
                    for (int i = 0; i < selectableLineages.length; i++) {
                        if (lineages != null && i < lineages.length) {
                            selectableLineages[i].set(lineages[i]);
                        } else {
                            selectableLineages[i].set("");
                        }
                    }
                });

        for (int i = 0; i < selectableLineages.length; i++) {
            selectableLineages[i] = new ObservableString("");
        }
    }

    private void bindGivenBonuses() {
        // Bind givenBonuses to track the total number of bonuses given
        Runnable updateGivenBonuses = () -> {
            int total = 0;
            for (ObservableBoolean plusOne : abilityPlusOnes) {
                if (plusOne.get()) {
                    total++;
                }
            }
            for (ObservableBoolean plusTwo : abilityPlusTwos) {
                if (plusTwo.get()) {
                    total += 2;
                }
            }
            givenBonuses.set(total);
        };
        for (ObservableBoolean plusOne : abilityPlusOnes) {
            plusOne.addListener(_ -> updateGivenBonuses.run());
        }
        for (ObservableBoolean plusTwo : abilityPlusTwos) {
            plusTwo.addListener(_ -> updateGivenBonuses.run());
        }
    }

    private void bindGivenSkills() {
        // Bind givenSkills to track the total number of skill proficiencies given
        Runnable updateGivenSkills = () -> {
            int total = 0;
            for (ObservableBoolean skillProficiency : skillProficiencies) {
                if (skillProficiency.get()) {
                    total++;
                }
            }
            for (ObservableBoolean fixedSkill : fixedSkills) {
                if (fixedSkill.get()) {
                    total--;
                }
            }
            givenSkills.set(total);
        };

        for (ObservableBoolean skillProficiency : skillProficiencies) {
            skillProficiency.addListener(_ -> updateGivenSkills.run());
        }
        for (ObservableBoolean fixedSkill : fixedSkills) {
            fixedSkill.addListener(_ -> updateGivenSkills.run());
        }
    }

    private void bindCreatureType() {
        species.addListener(
                (newVal) -> {
                    creatureType.set(getString(new String[] { "species", newVal, "type" }));
                });
    }

    private void bindAvailablePlusOne(int index) {
        Runnable updateAvailablePlusOne = () -> {
            Boolean bool = false;
            String[] possibleAbilities = getStrings(new String[] { "backgrounds", background.get(), "abilities" });
            if (Arrays.asList(possibleAbilities).contains(abilityNames[index])
                    && !abilityPlusTwos[index].get()
                    && (givenBonuses.get() < 3 || abilityPlusOnes[index].get())) {
                bool = true;
            }
            if (!availablePlusOnes[index].get().equals(bool)) {
                availablePlusOnes[index].set(bool);
            }
        };

        background.addListener(_ -> updateAvailablePlusOne.run());
        abilityPlusOnes[index].addListener(_ -> updateAvailablePlusOne.run());
        abilityPlusTwos[index].addListener(_ -> updateAvailablePlusOne.run());
        givenBonuses.addListener(_ -> updateAvailablePlusOne.run());

        availablePlusOnes[index].addListener(
                (newVal) -> {
                    if (!newVal && abilityPlusOnes[index].get()) {
                        abilityPlusOnes[index].set(false);
                    }
                });
    }

    private void bindAvailablePlusTwo(int index) {
        Runnable updateAvailablePlusTwo = () -> {
            Boolean bool = false;
            String[] possibleAbilities = getStrings(new String[] { "backgrounds", background.get(), "abilities" });
            if (Arrays.asList(possibleAbilities).contains(abilityNames[index])
                    && !abilityPlusOnes[index].get()
                    && (givenBonuses.get() < 2 || abilityPlusTwos[index].get())) {
                bool = true;
            }
            if (!availablePlusTwos[index].get().equals(bool)) {
                availablePlusTwos[index].set(bool);
            }
        };

        background.addListener(_ -> updateAvailablePlusTwo.run());
        abilityPlusOnes[index].addListener(_ -> updateAvailablePlusTwo.run());
        abilityPlusTwos[index].addListener(_ -> updateAvailablePlusTwo.run());
        givenBonuses.addListener(_ -> updateAvailablePlusTwo.run());

        availablePlusTwos[index].addListener(
                (newVal) -> {
                    if (!newVal && abilityPlusTwos[index].get()) {
                        abilityPlusTwos[index].set(false);
                    }
                });
    }

    private void bindAvailablePluses(int index) {
        Runnable updateAvailablePluses = () -> {
            int value = abilityBases[index].get();
            int threshold = 1;
            if (value >= 13) {
                threshold = 2;
            }
            if (generationPoints.get() >= threshold && value < 15) {
                availablePluses[index].set(true);
            } else {
                availablePluses[index].set(false);
            }
        };
        generationPoints.addListener(_ -> updateAvailablePluses.run());
        abilityBases[index].addListener(_ -> updateAvailablePluses.run());
    }

    private void bindAvailableMinuses(int index) {
        Runnable updateAvailableMinuses = () -> {
            int value = abilityBases[index].get();
            int threshold = 1;
            if (value > 13) {
                threshold = 2;
            }
            if (generationPoints.get() + threshold <= 27 && value > 8) {
                availableMinuses[index].set(true);
            } else {
                availableMinuses[index].set(false);
            }
        };
        generationPoints.addListener(_ -> updateAvailableMinuses.run());
        abilityBases[index].addListener(_ -> updateAvailableMinuses.run());
    }

    private void bindAvailableSkills(int index) {
        Runnable updateAvailableSkills = () -> {
            String[] possibleSkills = getStrings(new String[] { "classes", classes[0].get(), "skills" });
            maxSkills = getInt(new String[] { "classes", classes[0].get(), "skills_number" });
            if (Arrays.asList(possibleSkills).contains(skillNames[index])
                    && !fixedSkills[index].get()
                    && (givenSkills.get() < maxSkills || skillProficiencies[index].get())) {
                availableSkills[index].set(true);
            } else {
                availableSkills[index].set(false);
            }
        };
        classes[0].addListener(_ -> updateAvailableSkills.run());
        givenSkills.addListener(_ -> updateAvailableSkills.run());
        fixedSkills[index].addListener(_ -> updateAvailableSkills.run());

        availableSkills[index].addListener(
            (newVal) -> {
                if (!newVal && skillProficiencies[index].get() && !fixedSkills[index].get()) {
                    skillProficiencies[index].set(false);
                }
            });
    }

    private void bindFixedSkills() {
        background.addListener(
            (newVal) -> {
                String[] possibleSkills = getStrings(new String[] { "backgrounds", newVal, "skills" });
                for (int i = 0; i < fixedSkills.length; i++) {
                    fixedSkills[i].set(java.util.Arrays.asList(possibleSkills).contains(skillNames[i]));
                }
            });

        for (int i = 0; i < fixedSkills.length; i++) {
            fixedSkills[i] = new ObservableBoolean(false);
        }
    }

    private void bindFinalAbility(int index) {
        // Bind finalAbilities to track (abilityBases + bonuses), defaulting to 10 if
        // abilityBases[index] is 0
        Runnable updateFinalAbility = () -> {
            int base = abilityBases[index].get(); // ObservableInteger
            int one = abilityPlusOnes[index].get() ? 1 : 0;
            int two = abilityPlusTwos[index].get() ? 2 : 0;
            int featOne = 0;
            int featTwo = 0;
            int boonOne = 0;
            int boonTwo = 0;

            for (int i = 0; i < classes.length; i++) {
                for (int j = 0; j < feats[i].length; j++) {
                    String featName = featOnes[i][j].get();
                    if (featName != null && featName.equals(abilityNames[index])) {
                        if (getString(new String[] { "feats", feats[i][j].get(), "type" }).equals("EPIC_BOON")) {
                            boonOne++;
                        } else {
                            featOne++;
                        }
                    }

                    featName = featTwos[i][j].get();
                    if (featName != null && featName.equals(abilityNames[index])) {
                        if (getString(new String[] { "feats", feats[i][j].get(), "type" }).equals("EPIC_BOON")) {
                            boonTwo++;
                        } else {
                            featTwo++;
                        }
                    }
                }
            }

            abilities[index].set(Math.min(
                    Math.min((base != 0 ? base : 10) + one + two + featOne + featTwo, 20) + boonOne + boonTwo, 30));
        };
        abilityBases[index].addListener(_ -> updateFinalAbility.run());
        abilityPlusOnes[index].addListener(_ -> updateFinalAbility.run());
        abilityPlusTwos[index].addListener(_ -> updateFinalAbility.run());

        for (int i = 0; i < classes.length; i++) {
            for (int j = 0; j < feats[i].length; j++) {
                featOnes[i][j].addListener(_ -> updateFinalAbility.run());
                featTwos[i][j].addListener(_ -> updateFinalAbility.run());
            }
        }
    }

    private void bindAbilityModifier(int index) {
        // Bind the abilityModifier to the corresponding ability
        Runnable updateAbilityModifier = () -> {
            int ability = abilities[index].get();
            int modifier = (ability - 10) / 2;
            abilityModifiers[index].set(modifier);
        };
        abilities[index].addListener(_ -> updateAbilityModifier.run());
    }

    private void bindSavingThrowProficiencies(int index) {
        // Bind the savingThrowProficiencies to the corresponding ability
        Runnable updateSavingThrowProficiencies = () -> {
            String[] possibleSaves = getStrings(new String[] { "classes", classes[0].get(), "abilities" });
            savingThrowProficiencies[index].set(Arrays.asList(possibleSaves).contains(abilityNames[index]));
        };
        classes[0].addListener(_ -> updateSavingThrowProficiencies.run());
    }

    private void bindSavingThrowBonus(int index) {
        // Bind savingThrowBonuses[index] to include proficiencyBonus if
        // savingThrowProficiencies[index] is true
        // Uselessely convoluted, but it refused to believe that proficiencyBonus can't
        // be null. I don't like seeing warnings, so I did this.
        Runnable updateSavingThrowBonus = () -> {
            Boolean prof = savingThrowProficiencies[index].get();
            Integer bonus = proficiencyBonus.get();
            savingThrowBonuses[index].set((prof != null && prof && bonus != null) ? bonus : 0);
        };
        savingThrowProficiencies[index].addListener(_ -> updateSavingThrowBonus.run());
        proficiencyBonus.addListener(_ -> updateSavingThrowBonus.run());
    }

    private void bindGenerationPoints() {
        Runnable updateGenerationPoints = () -> {
            int points = 27;
            if (generationMethod.get().equals("POINT_BUY")) {
                for (ObservableInteger abilityBase : abilityBases) {
                    int value = abilityBase.get();
                    if (value > 8) {
                        int upTo13 = Math.min(value, 13) - 8;
                        int above13 = Math.max(0, value - 13);
                        points -= upTo13 * 1 + above13 * 2;
                    }
                }
            }
            generationPoints.set(points);
        };

        generationMethod.addListener(_ -> updateGenerationPoints.run());
        for (ObservableInteger abilityBase : abilityBases) {
            abilityBase.addListener(_ -> updateGenerationPoints.run());
        }
    }

    private void bindGenerationMethod() {
        generationMethod.addListener(
                (newVal) -> {
                    if (newVal.equals("STANDARD_ARRAY")) {
                        for (ObservableString abilityBaseShown : abilityBasesShown) {
                            abilityBaseShown.set("RANDOM");
                        }
                    } else {

                    }
                    if (newVal.equals("POINT_BUY")) {
                        // reset all abilities to 8
                        for (ObservableInteger abilityBase : abilityBases) {
                            abilityBase.set(8);
                        }
                    } else {

                    }
                    if (newVal.equals("CUSTOM_M")) {

                    } else {

                    }
                    if (newVal.equals("RANDOM")) {
                        for (ObservableString abilityBaseShown : abilityBasesShown) {
                            abilityBaseShown.set("RANDOM");
                        }
                    } else {

                    }
                });
    }

    private void bindSavingThrowModifier(int index) {
        // Bind the abilityModifier to the corresponding ability
        Runnable updateSavingThrowModifier = () -> {
            savingThrowModifiers[index].set(
                    abilityModifiers[index].get() + savingThrowBonuses[index].get());
        };
        abilityModifiers[index].addListener(_ -> updateSavingThrowModifier.run());
        savingThrowBonuses[index].addListener(_ -> updateSavingThrowModifier.run());
    }

    private void bindSkillModifier(int index) {
        // Bind the skillModifier to the corresponding ability
        Runnable updateSkillModifier = () -> {
            skillModifiers[index].set(
                    abilityModifiers[skillAbilities[index]].get() + skillBonuses[index].get());
        };
        abilityModifiers[skillAbilities[index]].addListener(_ -> updateSkillModifier.run());
        skillBonuses[index].addListener(_ -> updateSkillModifier.run());
    }

    private void bindSkillBonus(int index) {
        Runnable updateSkillBonus = () -> {
            Boolean isProficient = skillProficiencies[index].get();
            Integer profBonus = proficiencyBonus.get();
            skillBonuses[index].set(
                    (isProficient != null && isProficient && profBonus != null) ? profBonus : 0);
        };
        skillProficiencies[index].addListener(_ -> updateSkillBonus.run());
        proficiencyBonus.addListener(_ -> updateSkillBonus.run());
    }

    private void bindSkillProficiency(int index) {
        // Bind the skillModifier to the corresponding ability
        skillProficiencies[index] = new ObservableBoolean(false);

        fixedSkills[index].addListener(
            (newVal) -> {
                if (newVal || !availableSkills[index].get()) {
                    skillProficiencies[index].set(newVal);
                }
            });
    }

    private void bindSpeed() {
        Runnable updateSpeed = () -> {
            int baseSpeed = getInt(new String[] { "species", species.get(), "speed" });
            if (baseSpeed > 0) {
                speed.set(baseSpeed);
            } else {
                speed.set(30);
            }

            if (grappled.get() || paralyzed.get() || petrified.get() || restrained.get() || unconscious.get()) {
                speed.set(0);
            } else {
                int armorMalus = 0;
                if (abilities[0].get() < armor.get().getStrength()) {
                    armorMalus = 10;
                }
                speed.set(Math.max(speed.get() - exhaustion.get() * 5 - armorMalus, 0));
            }
        };
        species.addListener(_ -> updateSpeed.run());
        lineage.addListener(_ -> updateSpeed.run());
        exhaustion.addListener(_ -> updateSpeed.run());
        grappled.addListener(_ -> updateSpeed.run());
        paralyzed.addListener(_ -> updateSpeed.run());
        petrified.addListener(_ -> updateSpeed.run());
        restrained.addListener(_ -> updateSpeed.run());
        unconscious.addListener(_ -> updateSpeed.run());
        abilities[0].addListener(_ -> updateSpeed.run());
        armor.addListener(_ -> updateSpeed.run());
    }

    private void bindDarkvision() {
        Runnable updateDarkvision = () -> {
            int baseDarkvision = getInt(new String[] { "species", species.get(), "darkvision" });
            darkvision.set(baseDarkvision);
        };
        species.addListener(_ -> updateDarkvision.run());
        lineage.addListener(_ -> updateDarkvision.run());
    }

    private void bindArmorClass() {
        Runnable updateArmorClass = () -> {
            int base = armor.get().getArmorClass();
            int dexterity = armor.get().getDexterity();
            int shieldAC = 0;
            if (hasShieldProficiency.get()) {
                shieldAC = shield.get().getArmorClass(); // If shield is equipped and has proficiency, add its AC
            }
            int modifier;
            switch (dexterity) {
                case 1 -> modifier = 0; // no DEX bonus
                case 2 -> modifier = Math.max(abilityModifiers[1].get(), 2); // max +2 bonus
                default -> modifier = abilityModifiers[1].get();
            }
            armorClass.set(
                    Math.max(base, 10) + modifier + shieldAC);
        };
        abilityModifiers[1].addListener(_ -> updateArmorClass.run());
        armorProficiencies.addListener(_ -> updateArmorClass.run());
        armor.addListener(_ -> updateArmorClass.run());
        shield.addListener(_ -> updateArmorClass.run());
    }

    private void bindLanguages() {
        Runnable updateLanguages = () -> {
            String[] commonLanguages = getStrings(new String[] {"common_languages"});
            List<String> languagesList = new ArrayList<>();
            languagesList.addAll(Arrays.asList(commonLanguages));

            if (languagesList.contains(languageOne.get())) {
                languagesList.remove(languageOne.get());
            }
            if (languagesList.contains(languageTwo.get())) {
                languagesList.remove(languageTwo.get());
            }
            languagesList.add("RANDOM");
            selectableLanguages.setAll(languagesList);
        };

        languageOne.addListener(_ -> updateLanguages.run());
        languageTwo.addListener(_ -> updateLanguages.run());
        updateLanguages.run();
    }

    private void bindProficiencyBonus() {
        Runnable updateProficiencyBonus = () -> {
            totalLevel.set(0);
            for (ObservableInteger levelValue : levels) {
                totalLevel.set(totalLevel.get() + levelValue.get());
            }
            proficiencyBonus.set(
                    (totalLevel.get() + 7) / 4);
        };
        for (int i = 0; i < classes.length; i++) {
            levels[i].addListener(_ -> updateProficiencyBonus.run());
        }
    }

    private void bindInitiativeBonus() {
        Runnable updateInitiativeBonus = () -> {
            initiativeBonus.set(
                    abilityModifiers[1].get());
        };
        abilityModifiers[1].addListener(_ -> updateInitiativeBonus.run());
    }

    private void bindHealth() {
        Runnable updateHealth = () -> {
            int oldHealth = health.get();
            int fix = hitDies[0].get() + abilityModifiers[2].get();
            int var = 0;
            int[] newMaximumHitDies = { 0, 0, 0, 0 };

            for (int i = 0; i < classes.length; i++) {
                int isFirstClass = 0;
                if (i == 0) {
                    isFirstClass = 1;
                }
                fix += Math.max((levels[i].get() - isFirstClass) * abilityModifiers[2].get(), 0);
                if (healthMethod.get().equals("MEDIUM_HP")) {
                    var += Math.max((levels[i].get() - isFirstClass) * (hitDies[i].get() / 2 + 1), 0);
                } else if (healthMethod.get().equals("RANDOM")) {
                    var += 0;
                }

                switch (hitDies[i].get()) {
                    case 6 -> newMaximumHitDies[0] += levels[i].get();
                    case 8 -> newMaximumHitDies[1] += levels[i].get();
                    case 10 -> newMaximumHitDies[2] += levels[i].get();
                    case 12 -> newMaximumHitDies[3] += levels[i].get();
                }
            }

            for (int i = 0; i < 4; i++) {
                if (newMaximumHitDies[i] > maximumHitDies[i].get()) {
                    availableHitDies[i].set(
                            availableHitDies[i].get() + (newMaximumHitDies[i] - maximumHitDies[i].get()));
                } else if (availableHitDies[i].get() > newMaximumHitDies[i]) {
                    availableHitDies[i].set(newMaximumHitDies[i]);
                }

                maximumHitDies[i].set(newMaximumHitDies[i]);
            }

            fixedHealth.set(Math.max(fix, 1));
            health.set(Math.max(fixedHealth.get() + var, 1));

            if (health.get() > oldHealth) {
                currentHealth.set(currentHealth.get() + (health.get() - oldHealth));
            }
        };

        for (int i = 0; i < classes.length; i++) {
            levels[i].addListener(_ -> updateHealth.run());
            hitDies[i].addListener(_ -> updateHealth.run());
        }
        healthMethod.addListener(_ -> updateHealth.run());
        abilityModifiers[2].addListener(_ -> updateHealth.run());

        Runnable maxHealth = () -> {
            if (currentHealth.get() > health.get()) {
                currentHealth.set(health.get());
            }
        };
        health.addListener(_ -> maxHealth.run());
        currentHealth.addListener(_ -> maxHealth.run());

        currentHealthShown.addListener((newValue) -> {
            if (newValue != null) {
                try {
                    // Attempt to parse the new value as an integer
                    int parsedValue = Integer.parseInt(newValue);
                    currentHealth.set(parsedValue); // Update property
                } catch (NumberFormatException e) {
                    // Handle the case where the value is not a valid integer
                    System.err.println("Warning: Invalid health value: " + newValue);
                    currentHealth.set(health.get()); // Set a default value
                }
            } else {
                currentHealth.set(health.get()); // Set a default value
            }
        });

        currentHealth.addListener((newValue) -> {
            if (newValue != null) {
                currentHealthShown.set(String.valueOf(newValue));
            }
        });
    }

    private void bindHitDie() {
        for (int i = 0; i < classes.length; i++) {
            int index = i;
            Runnable updateHitDie = () -> {
                int baseHitDie = getInt(new String[] { "classes", classes[index].get(), "hit_die" });
                hitDies[index].set(baseHitDie > 0 ? baseHitDie : 4);
            };
            classes[index].addListener(_ -> updateHitDie.run());
        }
    }

    private void bindMoneys() {
        for (int i = 0; i < moneys.length; i++) {
            int index = i;
            moneysShown[index].addListener((newValue) -> {
                if (newValue != null && !newValue.equals("")) {
                    try {
                        // Attempt to parse the new value as an integer
                        int parsedValue = Integer.parseInt(newValue);
                        moneys[index].set(parsedValue); // Update the money property
                    } catch (NumberFormatException e) {
                        // Handle the case where the value is not a valid integer
                        System.err.println("Warning: Invalid money value: " + newValue);
                        moneys[index].set(0); // Set a default value (e.g., 0)
                    }
                } else {
                    moneys[index].set(0); // Set a default value
                }
            });

            moneys[index].addListener((newValue) -> {
                if (newValue != null) {
                    moneysShown[index].set(String.valueOf(newValue));
                }
            });
        }
    }

    private void bindLevel() {
        for (int i = 0; i < classes.length; i++) {
            int index = i;
            levelsShown[index].addListener((newValue) -> {
                if (newValue != null) {
                    if (newValue.equals("RANDOM")) {
                        // Handle the "RANDOM" case explicitly
                        levels[index].set(0); // Set level to 0 or any default value for "RANDOM"
                    } else {
                        try {
                            // Attempt to parse the new value as an integer
                            int parsedValue = Integer.parseInt(newValue);
                            levels[index].set(parsedValue); // Update the level property
                        } catch (NumberFormatException e) {
                            // Handle the case where the value is not a valid integer
                            System.err.println("Warning: Invalid level value: " + newValue);
                            levels[index].set(0); // Set a default value (e.g., 0)
                        }
                    }
                } else {
                    levels[index].set(0); // Set a default value
                }
            });

            // Update `levelShown` when `level` changes
            levels[index].addListener((newValue) -> {
                if (newValue != null) {
                    if (newValue == 0) {
                        // If level is 0, set `levelShown` to "RANDOM"
                        levelsShown[index].set("RANDOM");
                    } else {
                        // Otherwise, set `levelShown` to the string representation of the level
                        levelsShown[index].set(String.valueOf(newValue));
                    }
                }
            });
        }
    }

    private void bindAbilityBase(int index) {
        Runnable bindSelectableAbilities = () -> {
            List<String> standardList = new ArrayList<>();
            for (int standard : standardArray) {
                standardList.add(String.valueOf(standard));
            }
            for (ObservableString abilityBase : abilityBasesShown) {
                if (standardList.contains(abilityBase.get())) {
                    standardList.remove(abilityBase.get());
                }
            }
            standardList.add("RANDOM");
            selectableAbilities.setAll(standardList);
        };
        bindSelectableAbilities.run();

        abilityBasesShown[index].addListener((newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                if (newValue.equals("RANDOM")) {
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

            bindSelectableAbilities.run();
        });

        abilityBases[index].addListener((newValue) -> {
            if (newValue != null) {
                if (newValue == 0) {
                    abilityBasesShown[index].set("RANDOM");
                } else {
                    abilityBasesShown[index].set(String.valueOf(newValue));
                }
            }
        });
    }

    private void bindTraits() {
        Runnable updateTraits = () -> {
            traits.clear(); // Keep the list in sync
            String[] traitNames = getStrings(new String[] { "species", species.get(), "traits" });
            if (traitNames != null) {
                for (String trait : traitNames) {
                    traits.add(trait);
                }
            }

            traitNames = getStrings(new String[] { "species", species.get(), "lineages", lineage.get(), "traits" });
            if (traitNames != null) {
                for (String trait : traitNames) {
                    traits.add(trait);
                }
            }

            traitNames = getStrings(new String[] { "feats", originFeat.get(), "traits" });
            if (traitNames != null) {
                for (String trait : traitNames) {
                    traits.add(trait);
                }
            }

            for (int i = 0; i < classes.length; i++) {
                traitNames = getStrings(new String[] { "classes", classes[i].get(), "traits" });
                if (traitNames != null) {
                    for (String trait : traitNames) {
                        if (levels[i].get() >= getInt(new String[] { "classes", classes[i].get(), "traits", trait }) && !traits.getList().contains(trait)) {
                            traits.add(trait);
                        }
                    }
                }

                traitNames = getStrings(new String[] { "classes", classes[i].get(), "subclasses", subclasses[i].get(), "traits" });
                if (traitNames != null) {
                    for (String trait : traitNames) {
                        if (levels[i].get() >= getInt(new String[] { "classes", classes[i].get(), "subclasses",subclasses[i].get(), "traits", trait }) && !traits.getList().contains(trait)) {
                            traits.add(trait);
                        }
                    }
                }

                for (ObservableString feat : feats[i]) {
                    if (possibleFeats.getList().get(i).getList().contains(feat.get())) {
                        traitNames = getStrings(new String[] { "feats", feat.get(), "traits" });
                        if (traitNames != null) {
                            for (String trait : traitNames) {
                                if (!traits.getList().contains(trait)) {
                                    traits.add(trait);
                                }
                            }
                        }
                    }
                }
            }
        };

        species.addListener(_ -> updateTraits.run());
        lineage.addListener(_ -> updateTraits.run());
        originFeat.addListener(_ -> updateTraits.run());
        for (int i = 0; i < classes.length; i++) {
            levels[i].addListener(_ -> updateTraits.run());
            for (ObservableString feat : feats[i]) {
                feat.addListener(_ -> updateTraits.run());
            }
            classes[i].addListener(_ -> updateTraits.run());
            subclasses[i].addListener(_ -> updateTraits.run());
        }
        updateTraits.run();
    }

    private void bindFeatsAbilities() {
        for (int i = 0; i < classes.length; i++) {
            int classIndex = i;
            for (int j = 0; j < feats[classIndex].length; j++) {
                int index = j;
                Runnable updateFeatAbilities = () -> {
                    String[] featAbilities = getStrings(
                            new String[] { "feats", feats[classIndex][index].get(), "abilities" });
                    for (int k = 0; k < featsAbilities[classIndex][index].length; k++) {
                        if (k < featAbilities.length) {
                            featsAbilities[classIndex][index][k].set(featAbilities[k]);
                        } else {
                            featsAbilities[classIndex][index][k].set("");
                        }
                    }
                };
                feats[classIndex][index].addListener(_ -> updateFeatAbilities.run());
                updateFeatAbilities.run();
            }
        }
    }

    private void bindWeaponProficiencies() {
        Runnable updateWeaponProficiencies = () -> {
            weaponProficiencies.clear();
            List<String> takenWeapons = new ArrayList<>();

            for (ObservableString classe : classes) {
                String[] weapons = getStrings(new String[] { "classes", classe.get(), "weapons" });
                if (weapons != null) {
                    for (String weapon : weapons) {
                        if (weapon != null && !takenWeapons.contains(weapon)) {
                            weaponProficiencies.add(weapon);
                            takenWeapons.add(weapon);
                        }
                    }
                }
            }
        };

        updateWeaponProficiencies.run();
        for (ObservableString classe : classes) {
            classe.addListener(_ -> updateWeaponProficiencies.run());
        }
    }

    private void bindArmorProficiencies() {
        Runnable updateArmorProficiencies = () -> {
            armorProficiencies.clear();
            List<String> takenArmors = new ArrayList<>();

            for (ObservableString classe : classes) {
                String[] armors = getStrings(new String[] { "classes", classe.get(), "armors" });
                if (armors != null) {
                    for (String armorProficiency : armors) {
                        if (armorProficiency != null && !takenArmors.contains(armorProficiency)) {
                            armorProficiencies.add(armorProficiency);
                            takenArmors.add(armorProficiency);
                        }
                    }
                }
            }
        };

        updateArmorProficiencies.run();
        for (ObservableString classe : classes) {
            classe.addListener(_ -> updateArmorProficiencies.run());
        }
    }

    private void bindToolProficiencies() {
        Runnable updateToolProficiencies = () -> {
            List<String> takenTools = new ArrayList<>();
            List<String> newTools = new ArrayList<>();
            // in the 2024 rules only one tool proficiency is given for each background.
            // Done like this for future compatibility
            String[] tools = getStrings(new String[] { "backgrounds", background.get(), "tools" });
            if (tools != null) {
                for (String tool : tools) {
                    if (tool != null && !takenTools.contains(tool)) {
                        newTools.add(tool);
                        takenTools.add(tool);
                    }
                }
            }

            for (ObservableString classe : classes) {
                tools = getStrings(new String[] { "classes", classe.get(), "tools" });
                if (tools != null) {
                    for (String tool : tools) {
                        if (tool != null && !takenTools.contains(tool)) {
                            newTools.add(tool);
                            takenTools.add(tool);
                        }
                    }
                }
            }

            toolProficiencies.setAll(newTools); // Only triggers listeners once
        };

        background.addListener(_ -> updateToolProficiencies.run());
        for (ObservableString classe : classes) {
            classe.addListener(_ -> updateToolProficiencies.run());
        }
        updateToolProficiencies.run();
    }

    private void bindIncapacitated() {
        Runnable updateIncapacitated = () -> {
            if (paralyzed.get() || petrified.get() || stunned.get() || unconscious.get()) {
                incapacitation.set(true);
            } else {
                incapacitation.set(false);
            }
        };
        paralyzed.addListener(_ -> updateIncapacitated.run());
        petrified.addListener(_ -> updateIncapacitated.run());
        stunned.addListener(_ -> updateIncapacitated.run());
        unconscious.addListener(_ -> updateIncapacitated.run());
    }

    private void bindProne() {
        Runnable updateProne = () -> {
            if (unconscious.get()) {
                prone.set(true);
                proneness.set(true);
            } else {
                proneness.set(false);
            }
        };
        unconscious.addListener(_ -> updateProne.run());
    }

    private void bindSelectableFeats() {
        for (int classIndex = 0; classIndex < classes.length; classIndex++) {
            possibleFeats.getList().add(new CustomObservableList<>());
            selectableFeats.getList().add(new CustomObservableList<>());
            int index = classIndex;
            Runnable updateSelectableFeats = () -> {
                List<String> newFeats = new ArrayList<>();

                String[] selectableFeatsTotal = getSelectableFeats();
                for (String feat : selectableFeatsTotal) {
                    int[] stats = getInts(new String[] { "feats", feat, "stats" });
                    Boolean magic = getBoolean(new String[] { "feats", feat, "magic" });
                    String[] proficienciesArmor = getStrings(new String[] { "feats", feat, "armorProficiencies" });
                    String type = getString(new String[] { "feats", feat, "type" });
                    Boolean valid = true;

                    Boolean found;
                    for (String proficiency : proficienciesArmor) {
                        found = false;
                        for (String obsStr : armorProficiencies.asList()) {
                            if (obsStr != null && proficiency.equals(obsStr)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            valid = false;
                        }
                    }

                    found = true;

                    for (int i = 0; i < stats.length && valid; i++) {
                        if (stats[i] > 0) {
                            found = false;
                            if (abilities[i].get() >= stats[i]) {
                                found = true;
                                break;
                            }
                        }
                    }

                    if (!found) {
                        valid = false;
                    }

                    if (magic && !Arrays.asList(getMagicClasses()).contains(classes[index].get())) {
                        valid = false;
                    }

                    Boolean epicBoon = false;
                    for (ObservableString actualFeat : feats[index]) {
                        if (getString(new String[] { "feats", actualFeat.get(), "type" }).equals("EPIC_BOON")
                                && !actualFeat.get().equals(feat)) {
                            epicBoon = true;
                            break;
                        }
                    }

                    if (!feat.equals(originFeat.get())
                            && (valid || type.equals("ORIGIN"))
                            && levels[index].get() >= getInt(new String[] { "feats", feat, "level" })
                            && (!type.equals("EPIC_BOON") || !epicBoon)) {
                        newFeats.add(feat);
                    }
                }
                possibleFeats.getList().get(index).setAll(newFeats);

                // Avoid duplicate non repeatable feats
                List<String> removeFeats = new ArrayList<>();
                for (String newFeat : newFeats) {
                    for (int i = 0; i < classes.length; i++) {
                        for (ObservableString feat : feats[i]) {
                            if (newFeat.equals(feat.get())
                                    && !getBoolean(new String[] { "feats", feat.get(), "repeatable" })) {
                                removeFeats.add(newFeat);
                            }
                        }
                    }

                    if (originFeat.get().equals(newFeat)
                            && !getBoolean(new String[] { "feats", newFeat, "repeatable" })) {
                        removeFeats.add(newFeat);
                    }
                }
                newFeats.removeAll(removeFeats);

                newFeats.add("RANDOM");
                selectableFeats.getList().get(index).setAll(newFeats);
            };

            originFeat.addListener(_ -> updateSelectableFeats.run());
            levels[index].addListener(_ -> updateSelectableFeats.run());
            classes[index].addListener(_ -> updateSelectableFeats.run());
            for (ObservableInteger ability : abilities) {
                ability.addListener(_ -> updateSelectableFeats.run());
            }
            for (ObservableString feat : feats[index]) {
                feat.addListener(_ -> updateSelectableFeats.run());
            }
            updateSelectableFeats.run();
        }
    }

    private void bindClassEquipment() {
        classes[0].addListener((newVal) -> {
            classEquipment[0].set(newVal);

            String[] equipments = getStrings(new String[] { "classes", newVal, "equipment" });

            for (int i = 1; i < classEquipment.length; i++) {
                if (i - 1 < equipments.length) {
                    classEquipment[i].set("RANDOM");
                } else {
                    classEquipment[i].set("");
                }
            }
        });
    }

    private void bindBackgroundEquipment() {
        background.addListener((newVal) -> {
            backgroundEquipment[0].set(newVal);

            String[] equipments = getStrings(new String[] { "backgrounds", newVal, "equipment" });

            for (int i = 1; i < backgroundEquipment.length; i++) {
                if (i - 1 < equipments.length) {
                    backgroundEquipment[i].set("RANDOM");
                } else {
                    backgroundEquipment[i].set("");
                }
            }
        });
    }

    private void bindChoiceToolProficiencies() {
        toolProficiencies.addListener((newVal) -> {
            List<Proficiency> newChoices = new ArrayList<>();
            for (String equipment : newVal.asList()) {
                if (Arrays.asList(sets).contains(equipment)) {
                    newChoices.add(new Proficiency("RANDOM", equipment));
                }
            }

            for (Proficiency choice : choiceToolProficiencies.asList()) {
                for (Proficiency newChoice : newChoices) {
                    if (newChoice.getStrings().equals(choice.getStrings()) && newChoice.getName().equals("RANDOM")) {
                        newChoice.setName(choice.getName());
                    }
                }
            }

            choiceToolProficiencies.setAll(newChoices);
        });
    }

    private void bindSpells() {
        for (ObservableString _ : classes) {
            selectableSpells.getList().add(new CustomObservableList<>());
            selectableCantrips.getList().add(new CustomObservableList<>());
        }

        for (int i = 0; i < classes.length; i++) {
            int index = i;

            Runnable updateSpells = () -> {
                // TODO: redundant, turn into a property (maximum level too)
                float magicLevels = 0;
                for (int j = 0; j < classes.length; j++) {
                    if (Arrays.asList(getMagicClasses()).contains(classes[j].get())
                            && getBoolean(new String[] { "magic_classes", classes[j].get() })) {
                        magicLevels += levels[j].get();
                    } else if (Arrays.asList(getMagicClasses()).contains(classes[j].get())) {
                        magicLevels += levels[j].get() / 2.0;
                    }
                }
                int magicLevel = (int) Math.ceil(magicLevels);

                selectableSpells.getList().get(index).clear();
                selectableCantrips.getList().get(index).clear();

                int[] slots = getInts(new String[] { "spell_slots", String.valueOf(magicLevel) });
                int maximumLevel = 0;
                for (int j = 0; j < spellSlots.length; j++) {
                    int oldSlots = spellSlots[j].get();
                    if (slots[j] > oldSlots && availableSpellSlots[j].get() < slots[j]) {
                        availableSpellSlots[j].set(availableSpellSlots[j].get() + slots[j] - oldSlots);
                    }
                    spellSlots[j].set(slots[j]);
                    if (slots[j] > 0) {
                        maximumLevel++;
                    }
                }

                if ((Arrays.asList(getMagicClasses()).contains(classes[index].get())) && levels[index].get() > 0) {
                    String[] spellsList = getAllSpells();
                    for (String spell : spellsList) {
                        int spellLevel = getSpellInt(new String[] { spell, "level" });
                        String[] acceptedClasses = getSpellGroup(new String[] { spell, "lists" });

                        if (spellLevel <= maximumLevel
                                && Arrays.asList(acceptedClasses).contains(classes[index].get())) {
                            if (spellLevel > 0) {
                                selectableSpells.getList().get(index).add(new Spell(spell,
                                        getString(new String[] { "classes", classes[index].get(), "change" }),
                                        new String[0], getAbilityIndex(spellcastingAbilities[index].get()),
                                        getBoolean(new String[] { "classes", classes[index].get(), "limited" })));
                            } else if (levels[index].get() >= 1) {
                                selectableCantrips.getList().get(index).add(new Spell(spell, "LEVEL_UP", new String[0],
                                        getAbilityIndex(spellcastingAbilities[index].get()), true));
                            }
                        }
                    }
                }
            };
            for (int j = 0; j < classes.length; j++) {
                classes[j].addListener(_ -> updateSpells.run());
                levels[j].addListener(_ -> updateSpells.run());
            }
        }
    }

    private void bindMaxSpells() {
        for (int i = 0; i < classes.length; i++) {
            int index = i;
            Runnable updateMaxSpells = () -> {
                int[] spellsPerLevel = (getInts(new String[] { "classes", classes[index].get(), "prepared" }));
                int[] cantripsPerLevel = (getInts(new String[] { "classes", classes[index].get(), "cantrips" }));
                if (levels[index].get() > 0) {
                    if (spellsPerLevel.length > 0) {
                        maxSpells[index].set(spellsPerLevel[levels[index].get() - 1]);
                    } else {
                        maxSpells[index].set(0);
                    }
                    if (cantripsPerLevel.length > 0) {
                        maxCantrips[index].set(cantripsPerLevel[levels[index].get() - 1]);
                    } else {
                        maxCantrips[index].set(0);
                    }
                } else {
                    maxSpells[index].set(0);
                    maxCantrips[index].set(0);
                }
            };
            classes[index].addListener(_ -> updateMaxSpells.run());
            levels[index].addListener(_ -> updateMaxSpells.run());
        }
    }

    private void bindSpellcastingAbility() {
        for (int i = 0; i < classes.length; i++) {
            int index = i;
            classes[index].addListener((newVal) -> {
                spellcastingAbilities[index].set(getString(new String[] { "classes", newVal, "spellcasting" }));
            });

            Runnable updateSpellcastingAbility = () -> {
                for (int j = 0; j < abilityNames.length; j++) {
                    if (abilityNames[j].equals(spellcastingAbilities[index].get())) {
                        spellcastingAbilityModifiers[index].set(abilityModifiers[j].get());
                        spellcastingAttackModifiers[index].set(abilityModifiers[j].get() + proficiencyBonus.get());
                        spellcastingSaveDCs[index].set(8 + spellcastingAttackModifiers[index].get());
                        break;
                    }
                }
            };
            spellcastingAbilities[index].addListener(_ -> updateSpellcastingAbility.run());
            proficiencyBonus.addListener(_ -> updateSpellcastingAbility.run());
            for (int j = 0; j < abilityNames.length; j++) {
                abilityModifiers[j].addListener(_ -> updateSpellcastingAbility.run());
            }
        }
    }

    private void bindSubclass() {
        for (int i = 0; i < classes.length; i++) {
            int index = i;
            classes[index].addListener(_ -> {
                subclasses[index].set("RANDOM");
            });
        }
    }

    private void bindHasProficiencies() {
        Runnable updateArmor = () -> {
            for (String prof : armorProficiencies.asList()) {
                if (Arrays.asList(armor.get().getTags()).contains(prof)) {
                    hasArmorProficiency.set(true);
                    return;
                }
            }
            hasArmorProficiency.set(false);
        };
        armorProficiencies.addListener(_ -> updateArmor.run());
        armor.addListener(_ -> updateArmor.run());

        Runnable updateShield = () -> {
            for (String prof : armorProficiencies.asList()) {
                if (prof.equals("SHIELDS")) {
                    hasShieldProficiency.set(true);
                    return;
                }
            }
            hasShieldProficiency.set(false);
        };
        armorProficiencies.addListener(_ -> updateShield.run());
        shield.addListener(_ -> updateShield.run());

        Runnable updateMainHand = () -> {
            // Check if character proficiencies include the main hand weapon
            if (mainHand.get().getNominative().equals("UNARMED_STRIKE")) {
                hasMainProficiency.set(true);
                return;
            } else {
                for (String prof : weaponProficiencies.asList()) {
                    String tag = getString(new String[] { "weapon categories", prof, "tag" });
                    String[] attributes = getStrings(new String[] { "weapon categories", prof, "attributes" });
                    if (Arrays.asList(mainHand.get().getTags()).contains(tag)) {
                        boolean hasAttributes = true;
                        for (String attribute : attributes) {
                            if (!Arrays.asList(mainHand.get().getTags()).contains(attribute)) {
                                hasAttributes = false;
                                break;
                            }
                        }
                        if (hasAttributes) {
                            hasMainProficiency.set(true);
                            return;
                        }
                    }
                }
            }
            hasMainProficiency.set(false);
        };
        weaponProficiencies.addListener(_ -> updateMainHand.run());
        mainHand.addListener(_ -> updateMainHand.run());

        Runnable updateOffHand = () -> {
            for (String prof : weaponProficiencies.asList()) {
                String tag = getString(new String[] { "weapon categories", prof, "tag" });
                String[] attributes = getStrings(new String[] { "weapon categories", prof, "attributes" });
                if (Arrays.asList(offHand.get().getTags()).contains(tag)) {
                    boolean hasAttributes = true;
                    for (String attribute : attributes) {
                        if (!Arrays.asList(offHand.get().getTags()).contains(attribute)) {
                            hasAttributes = false;
                            break;
                        }
                    }
                    if (hasAttributes) {
                        hasOffProficiency.set(true);
                        return;
                    }
                }
            }
            hasOffProficiency.set(false);
        };
        weaponProficiencies.addListener(_ -> updateOffHand.run());
        offHand.addListener(_ -> updateOffHand.run());
    }

    // Helpers
    private String[] getStrings(String[] group) {
        return GroupManager.getInstance().getStrings(group);
    }

    private String getString(String[] group) {
        return GroupManager.getInstance().getString(group);
    }

    private int getInt(String[] group) {
        return GroupManager.getInstance().getInt(group);
    }

    private int[] getInts(String[] group) {
        return GroupManager.getInstance().getInts(group);
    }

    private String[] getSelectableFeats() {
        return GroupManager.getInstance().getSelectableFeats();
    }

    private Boolean getBoolean(String[] group) {
        return GroupManager.getInstance().getBoolean(group);
    }

    private String[] getMagicClasses() {
        return GroupManager.getInstance().getMagicClasses();
    }

    private String[] getAllSpells() {
        return SpellManager.getInstance().getAllSpells();
    }

    private int getSpellInt(String[] group) {
        return SpellManager.getInstance().getSpellInt(group);
    }

    private String[] getSpellGroup(String[] group) {
        return SpellManager.getInstance().getSpellGroup(group);
    }

    public void addItem(String item) {
        ItemManager.getInstance().addItem(this, item);
    }

    public GameCharacter duplicate() {
        GameCharacter copy = new GameCharacter();
        copy.gender.set(this.gender.get());
        copy.languageOne.set(this.languageOne.get());
        copy.languageTwo.set(this.languageTwo.get());
        copy.species.set(this.species.get());
        copy.background.set(this.background.get());
        copy.alignment.set(this.alignment.get());
        copy.lineage.set(this.lineage.get());
        copy.size.set(this.size.get());
        copy.name.set(this.name.get());
        for (int i = 0; i < classes.length; i++) {
            copy.classes[i].set(this.classes[i].get());
            copy.levelsShown[i].set(this.levelsShown[i].get());
            copy.subclasses[i].set(this.subclasses[i].get());
            for (int j = 0; j < feats[i].length; j++) {
                copy.feats[i][j].set(this.feats[i][j].get());
                copy.featOnes[i][j].set(this.featOnes[i][j].get());
                copy.featTwos[i][j].set(this.featTwos[i][j].get());
            }
        }
        for (int i = 0; i < classEquipment.length; i++) {
            copy.classEquipment[i].set(this.classEquipment[i].get());
            copy.backgroundEquipment[i].set(this.backgroundEquipment[i].get());
        }
        copy.choiceToolProficiencies.setAll(choiceToolProficiencies.getList());
        copy.spells.setAll(spells.getList());
        copy.cantrips.setAll(cantrips.getList());
        copy.items.setAll(items.getList());
        copy.healthMethod.set(this.healthMethod.get());
        copy.health.set(this.health.get());
        copy.generationMethod.set(this.generationMethod.get());
        for (int i = 0; i < abilityBases.length; i++) {
            copy.abilityBases[i].set(this.abilityBases[i].get());
            copy.abilityPlusOnes[i].set(this.abilityPlusOnes[i].get());
            copy.abilityPlusTwos[i].set(this.abilityPlusTwos[i].get());
        }
        for (int i = 0; i < skillProficiencies.length; i++) {
            copy.skillProficiencies[i].set(this.skillProficiencies[i].get());
        }
        for (int i = 0; i < moneys.length; i++) {
            copy.moneys[i].set(this.moneys[i].get());
        }
        for (int i = 0; i < 9; i++) {
            copy.availableSpellSlots[i].set(availableSpellSlots[i].get());
        }
        copy.mainHand.set(mainHand.get());
        copy.offHand.set(offHand.get());
        copy.armor.set(armor.get());
        copy.shield.set(shield.get());
        copy.finesseAbility.set(finesseAbility.get());
        copy.userDescription.set(userDescription.get());
        copy.currentHealth.set(currentHealth.get());

        return copy;
    }

    public void fill(boolean firstTime) {
        if (gender.get().equals("RANDOM")) {
            String[] availableGenders = getStrings(new String[] { "genders" });
            gender.set(availableGenders[(int) (Math.random() * availableGenders.length)]);
        }

        if (languageOne.get().equals("RANDOM")) {
            String[] availableLanguages = getStrings(new String[] { "common_languages" });
            List<String> langList = new ArrayList<>(Arrays.asList(availableLanguages));
            if (!languageTwo.get().equals("RANDOM")) {
                langList.remove(languageTwo.get());
            }
            languageOne.set(langList.get((int) (Math.random() * langList.size())));
        }

        if (languageTwo.get().equals("RANDOM")) {
            String[] availableLanguages = getStrings(new String[] { "common_languages" });
            List<String> langList = new ArrayList<>(Arrays.asList(availableLanguages));
            if (!languageOne.get().equals("RANDOM")) {
                langList.remove(languageOne.get());
            }
            languageTwo.set(langList.get((int) (Math.random() * langList.size())));
        }

        for (ObservableString classe : classes) {
            if (classe.get().equals("RANDOM")) {
                classe.set(selectableClasses.getList().get((int) (Math.random() * selectableClasses.size())));
            }
        }

        if (species.get().equals("RANDOM")) {
            String[] availableSpecies = getStrings(new String[] { "species" });
            species.set(availableSpecies[(int) (Math.random() * availableSpecies.length)]);
        }

        if (background.get().equals("RANDOM")) {
            String[] availableBackgrounds = getStrings(new String[] { "backgrounds" });
            background.set(availableBackgrounds[(int) (Math.random() * availableBackgrounds.length)]);
        }

        if (alignment.get().equals("RANDOM")) {
            String[] availableAlignments = getStrings(new String[] { "alignments" });
            alignment.set(availableAlignments[(int) (Math.random() * availableAlignments.length)]);
        }

        fillLater(firstTime);
    }

    public void fillLater(boolean firstTime) {
        int requiredLevels = 0;
        for (int i = 0; i < classes.length; i++) {
            if (!classes[i].get().equals("NONE") && levelsShown[i].get().equals("RANDOM")) {
                requiredLevels++;
            }
        }
        for (int i = 0; i < classes.length; i++) {
            int index = i;
            if (!classes[index].get().equals("NONE") && levelsShown[index].get().equals("RANDOM")) {
                int randomLevel = (int) (Math.random() * (20 - totalLevel.get() - requiredLevels + 1)) + 1;
                levelsShown[index].set(String.valueOf(randomLevel));
                requiredLevels--;
            }

            if (subclasses[index].get().equals("RANDOM")) {
                // Filter out empty subclasses
                List<String> validSubclasses = new ArrayList<>();
                for (ObservableString subclassOption : selectableSubclasses[index]) {
                    if (!subclassOption.get().isEmpty()) {
                        validSubclasses.add(subclassOption.get());
                    }
                }
                if (!validSubclasses.isEmpty()) {
                    subclasses[index].set(validSubclasses.get((int) (Math.random() * validSubclasses.size())));
                }
            }
        }

        if (lineage.get().equals("RANDOM")) {
            // Filter out empty lineages
            List<String> validLineages = new ArrayList<>();
            for (ObservableString lineageOption : selectableLineages) {
                if (!lineageOption.get().isEmpty()) {
                    validLineages.add(lineageOption.get());
                }
            }
            if (!validLineages.isEmpty()) {
                lineage.set(validLineages.get((int) (Math.random() * validLineages.size())));
            }
        }

        if (size.get().equals("RANDOM")) {
            // Filter out empty sizes
            List<String> validSizes = new ArrayList<>();
            for (ObservableString sizeOption : selectableSizes) {
                if (!sizeOption.get().isEmpty()) {
                    validSizes.add(sizeOption.get());
                }
            }
            if (!validSizes.isEmpty()) {
                size.set(validSizes.get((int) (Math.random() * validSizes.size())));
            }
        }

        // Remove selected non repeatable feats
        for (int k = 0; k < classes.length; k++) {
            int classIndex = k;
            for (int i = 0; i < feats[classIndex].length; i++) {
                List<String> newFeats = selectableFeats.asList().get(classIndex).asList();
                for (int j = 0; j < feats[classIndex].length; j++) {
                    int index = j;
                    if (!feats[classIndex][j].get().equals("RANDOM") && availableFeats[classIndex].get() > j) {
                        if (!getBoolean(new String[] { "feats", feats[classIndex][j].get(), "repeatable" })) {
                            newFeats.removeIf(feat -> feat.equals(feats[classIndex][index].get()));
                        }
                    } else if (availableFeats[classIndex].get() <= j) {
                        break;
                    }
                }

                if (feats[classIndex][i].get().equals("RANDOM") && availableFeats[classIndex].get() > i) {
                    if (!newFeats.isEmpty()) {
                        feats[classIndex][i].set(newFeats.get((int) (Math.random() * newFeats.size())));
                    }
                } else if (availableFeats[classIndex].get() <= i) {
                    break;
                }
            }

            for (int i = 0; i < feats[classIndex].length; i++) {
                if (featOnes[classIndex][i].get().equals("RANDOM") && availableFeats[classIndex].get() > i) {
                    ObservableString[] featAbilities = featsAbilities[classIndex][i];
                    // Filter out empty strings
                    List<String> validAbilities = new ArrayList<>();
                    for (ObservableString ability : featAbilities) {
                        if (!ability.get().isEmpty()) {
                            validAbilities.add(ability.get());
                        }
                    }
                    if (!validAbilities.isEmpty()) {
                        featOnes[classIndex][i].set(validAbilities.get((int) (Math.random() * validAbilities.size())));
                    }
                }

                if (featTwos[classIndex][i].get().equals("RANDOM") && availableFeats[classIndex].get() > i) {
                    ObservableString[] featAbilities = featsAbilities[classIndex][i];
                    // Filter out empty strings
                    List<String> validAbilities = new ArrayList<>();
                    for (ObservableString ability : featAbilities) {
                        if (!ability.get().isEmpty()) {
                            validAbilities.add(ability.get());
                        }
                    }
                    if (!validAbilities.isEmpty()) {
                        featTwos[classIndex][i].set(validAbilities.get((int) (Math.random() * validAbilities.size())));
                    }
                }
            }
        }

        if (name.get().equals("")) {
            String[] availableNames = switch (gender.get()) {
                case "MALE" ->
                    getStrings(new String[] { "species", species.get(), "lineages", lineage.get(), "males" });
                case "FEMALE" ->
                    getStrings(new String[] { "species", species.get(), "lineages", lineage.get(), "females" });
                default -> new String[0];
            };
            String[] availableLasts = getStrings(
                    new String[] { "species", species.get(), "lineages", lineage.get(), "lasts" });

            if (availableNames == null || availableNames.length == 0) {
                availableNames = switch (gender.get()) {
                    case "MALE" -> getStrings(new String[] { "species", species.get(), "males" });
                    case "FEMALE" -> getStrings(new String[] { "species", species.get(), "females" });
                    default -> new String[0];
                };
            }
            if (availableLasts == null || availableLasts.length == 0) {
                availableLasts = getStrings(new String[] { "species", species.get(), "lasts" });
            }

            String firstName = "";
            String lastName = "";
            if (availableNames != null && availableNames.length > 0) {
                firstName = availableNames[(int) (Math.random() * availableNames.length)];
            }
            if (availableLasts != null && availableLasts.length > 0) {
                lastName = availableLasts[(int) (Math.random() * availableLasts.length)];
            }

            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                name.set(firstName + " " + lastName);
            } else if (!firstName.isEmpty()) {
                name.set(firstName);
            } else if (!lastName.isEmpty()) {
                name.set(lastName);
            }
        }

        if (firstTime) {
            if (classEquipment[0].get().equals("RANDOM")) {
                String[] possibleEquipments = new String[] { classes[0].get(), "GOLD" };
                classEquipment[0].set(possibleEquipments[(int) (Math.random() * possibleEquipments.length)]);
            }

            if (classEquipment[0].get().equals(classes[0].get())) {
                String[] equipments = getStrings(new String[] { "classes", classes[0].get(), "equipment" });
                int index = 1;
                for (String equipment : equipments) {
                    if (Arrays.asList(sets).contains(equipment)) {
                        String[] options = getStrings(new String[] { "sets", equipment });
                        classEquipment[index].set(options[(int) (Math.random() * options.length)]);
                        index++;
                    }
                }
            }

            if (backgroundEquipment[0].get().equals("RANDOM")) {
                String[] possibleEquipments = new String[] { background.get(), "GOLD" };
                backgroundEquipment[0].set(possibleEquipments[(int) (Math.random() * possibleEquipments.length)]);
            }

            if (backgroundEquipment[0].get().equals(background.get())) {
                String[] equipments = getStrings(new String[] { "backgrounds", background.get(), "equipment" });
                int index = 1;
                for (String equipment : equipments) {
                    if (Arrays.asList(sets).contains(equipment)) {
                        String[] options = getStrings(new String[] { "sets", equipment });
                        backgroundEquipment[index].set(options[(int) (Math.random() * options.length)]);
                        index++;
                    }
                }
            }

            switch (classEquipment[0].get()) {
                case ("GOLD") -> {
                    moneys[3].set(moneys[3].get() + getInt(new String[] { "classes", classes[0].get(), "gold" })); // Roll
                                                                                                                   // 4d6
                                                                                                                   // x10
                                                                                                                   // gold
                                                                                                                   // pieces
                }
                default -> {
                    for (String item : getStrings(new String[] { "classes", classes[0].get(), "equipment" })) {
                        addItem(item);
                    }
                    for (int i = 1; i < classEquipment.length; i++) {
                        if (!classEquipment[i].get().equals("RANDOM")) {
                            addItem(classEquipment[i].get());
                        }
                    }
                }
            }

            switch (backgroundEquipment[0].get()) {
                case ("GOLD") -> {
                    moneys[3].set(moneys[3].get() + getInt(new String[] { "backgrounds", background.get(), "gold" })); // Roll
                                                                                                                       // 4d6
                                                                                                                       // x10
                                                                                                                       // gold
                                                                                                                       // pieces
                }
                default -> {
                    for (String item : getStrings(new String[] { "backgrounds", background.get(), "equipment" })) {
                        addItem(item);
                    }
                    for (int i = 1; i < backgroundEquipment.length; i++) {
                        if (!backgroundEquipment[i].get().equals("RANDOM")) {
                            addItem(backgroundEquipment[i].get());
                        }
                    }
                }
            }
        }

        List<String> usedProficiencies = new ArrayList<>();
        for (String proficiency : toolProficiencies.asList()) {
            usedProficiencies.add(proficiency);
        }
        for (String proficiency : armorProficiencies.asList()) {
            usedProficiencies.add(proficiency);
        }
        for (String proficiency : weaponProficiencies.asList()) {
            usedProficiencies.add(proficiency);
        }
        for (Proficiency proficiency : choiceToolProficiencies.asList()) {
            if (!proficiency.getName().equals("RANDOM")) {
                usedProficiencies.add(proficiency.getName());
            }
        }
        for (Proficiency choiceToolProficiency : choiceToolProficiencies.asList()) {
            if (choiceToolProficiency.getName().equals("RANDOM")) {
                String[] options = getStrings(new String[] { "sets", choiceToolProficiency.getStrings() });
                // Remove used proficiencies
                List<String> availableOptions = new ArrayList<>();
                for (String option : options) {
                    if (!usedProficiencies.contains(option)) {
                        availableOptions.add(option);
                    }
                }
                choiceToolProficiency.setName(availableOptions.get((int) (Math.random() * availableOptions.size())));
                usedProficiencies.add(choiceToolProficiency.getName());
            }
        }

        for (int classIndex = 0; classIndex < classes.length; classIndex++) {
            if (!selectableSpells.getList().get(classIndex).isEmpty()
                    && maxSpells[classIndex].get() > spells.getList().get(classIndex).size()) {
                int n = maxSpells[classIndex].get() - spells.size();
                n = Math.min(n, selectableSpells.getList().get(classIndex).size());
                List<Spell> possibleSpells = selectableSpells.getList().get(classIndex).asList();

                for (int i = 0; i < n; i++) {
                    int randomIndex = (int) (Math.random() * possibleSpells.size());
                    Spell selectedSpell = possibleSpells.remove(randomIndex);
                    spells.getList().get(classIndex).add(selectedSpell);
                }
            }

            if (!selectableCantrips.asList().get(classIndex).isEmpty()
                    && maxCantrips[classIndex].get() > cantrips.getList().get(classIndex).size()) {
                int n = maxCantrips[classIndex].get() - cantrips.size();
                n = Math.min(n, selectableCantrips.size());
                List<Spell> possibleCantrips = new ArrayList<>(selectableCantrips.asList().get(classIndex).asList());

                for (int i = 0; i < n; i++) {
                    int randomIndex = (int) (Math.random() * possibleCantrips.size());
                    Spell selectedCantrip = possibleCantrips.remove(randomIndex);
                    cantrips.getList().get(classIndex).add(selectedCantrip);
                }
            }
        }

        if (healthMethod.get().equals("RANDOM")) {
            healthMethod.set("CUSTOM_M");
            int hp = 0;
            for (int i = 0; i < classes.length; i++) {
                hp += Math.max((levels[i].get() - 1) * (int) (Math.random() * hitDies[i].get()), 0);
            }
            health.set(fixedHealth.get() + hp);
        }

        switch (generationMethod.get()) {
            case "STANDARD_ARRAY" -> {
                for (int i = 0; i < abilityBasesShown.length; i++) {
                    if (abilityBasesShown[i].get().equals("RANDOM")) {
                        int randomIndex = (int) (Math.random() * selectableAbilities.size());
                        String score = selectableAbilities.getList().get(randomIndex);
                        abilityBasesShown[i].set(score);
                    }
                }
            }
            case "POINT_BUY" -> {
                // TODO: statistically impossible infinite loop
                while (generationPoints.get() > 0) {
                    int randomIndex = (int) (Math.random() * abilityBases.length);
                    ObservableInteger abilityBase = abilityBases[randomIndex];
                    int score = abilityBase.get();
                    int cost = 1;
                    if (score >= 13) {
                        cost = 2;
                    }
                    if (generationPoints.get() - cost >= 0) {
                        score++;
                        generationPoints.set(generationPoints.get() - cost);
                    }
                    abilityBase.set(score);
                }
            }
            case "RANDOM" -> {
                for (int i = 0; i < abilityBasesShown.length; i++) {
                    if (abilityBasesShown[i].get().equals("RANDOM")) {
                        abilityBases[i].set(ThrowManager.getInstance().rollFourDropLowest());
                    }
                }
            }
        }
        generationMethod.set("CUSTOM_M");

        while (givenBonuses.get() < 3) {
            int randomIndex = (int) (Math.random() * abilityBases.length);
            int oneOrTwo = (int) (Math.random() * 2);
            if (givenBonuses.get() > 1) {
                oneOrTwo = 0;
            }
            ObservableBoolean abilityPlusOne = abilityPlusOnes[randomIndex];
            ObservableBoolean abilityPlusTwo = abilityPlusTwos[randomIndex];
            ObservableBoolean availablePlusOne = availablePlusOnes[randomIndex];
            ObservableBoolean availablePlusTwo = availablePlusTwos[randomIndex];
            if (oneOrTwo == 0 && !abilityPlusOne.get() && availablePlusOne.get()) {
                abilityPlusOne.set(true);
            } else if (!abilityPlusTwo.get() && availablePlusTwo.get()) {
                abilityPlusTwo.set(true);
            }
        }

        while (givenSkills.get() < maxSkills) {
            int randomIndex = (int) (Math.random() * skillProficiencies.length);
            ObservableBoolean skillProficiency = skillProficiencies[randomIndex];
            ObservableBoolean availableSkill = availableSkills[randomIndex];
            if (!skillProficiency.get() && availableSkill.get()) {
                skillProficiency.set(true);
            }
        }
    }

    public Boolean save(Boolean newFile, Stage stage) {
        return CharacterSerializer.save(this, newFile, stage);
    }

    public static GameCharacter load(Stage stage) {
        GameCharacter NC = CharacterSerializer.load(stage);
        return NC;
    }

    public static int getAbilityIndex(String ability) {
        for (int i = 0; i < abilityNames.length; i++) {
            if (abilityNames[i].equals(ability)) {
                return i;
            }
        }
        return 0; // Default to first ability if not found
    }

    public static String getAbilityName(int index) {
        if (index >= 0 && index < abilityNames.length) {
            return abilityNames[index];
        }
        return abilityNames[0]; // Default to first ability if index is out of bounds
    }
}
