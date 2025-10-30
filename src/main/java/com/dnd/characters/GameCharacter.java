package com.dnd.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.SpellManager;
import com.dnd.TranslationManager;
import com.dnd.ThrowManager;
import com.dnd.utils.CustomObservableList;
import com.dnd.utils.ObservableBoolean;
import com.dnd.utils.ObservableInteger;
import com.dnd.utils.ObservableString;
import com.dnd.items.Proficiency;
import com.dnd.items.Spell;

public class GameCharacter {
    private final String[] skillNames; // Get the names of all skills
    private final String[] abilityNames;
    private final String[] sets;

    private final ObservableString creatureType = new ObservableString("");
    private final ObservableString languageOne = new ObservableString("RANDOM");
    private final ObservableString languageTwo = new ObservableString("RANDOM");
    private final ObservableString height = new ObservableString("");
    private final ObservableString weight = new ObservableString("");
    private final ObservableString name = new ObservableString("");
    private final ObservableString gender = new ObservableString("RANDOM");
    private final ObservableString subclass = new ObservableString("RANDOM");
    private final ObservableString lineage = new ObservableString("RANDOM");
    private final ObservableString alignment = new ObservableString("RANDOM");
    private final ObservableString generationMethod = new ObservableString("STANDARD_ARRAY");
    private final ObservableString healthMethod = new ObservableString("MEDIUM_HP");
    private final ObservableString levelShown = new ObservableString("RANDOM");
    private final ObservableString classe = new ObservableString("RANDOM");
    private final ObservableString species = new ObservableString("RANDOM");
    private final ObservableString background = new ObservableString("RANDOM");
    private final ObservableString size = new ObservableString("");
    private final ObservableString originFeat = new ObservableString("");
    private final ObservableString spellcastingAbility = new ObservableString("");
    private final ObservableString[] moneysShown = new ObservableString[5];
    private final ObservableString[] featOnes;
    private final ObservableString[] featTwos;
    private final ObservableString[] feats;
    private final ObservableString[] availableSizes;
    private final ObservableString[] availableSubclasses;
    private final ObservableString[] availableLineages;
    private final ObservableString[] abilityBasesShown;
    private final ObservableString[] classEquipment;
    private final ObservableString[] backgroundEquipment;
    private final ObservableString[][] featsAbilities;

    private final CustomObservableList<ObservableString> actives = new CustomObservableList<>();
    private final CustomObservableList<ObservableString> passives = new CustomObservableList<>();
    private final CustomObservableList<ObservableString> weaponProficiencies = new CustomObservableList<>();
    private final CustomObservableList<ObservableString> armorProficiencies = new CustomObservableList<>();
    private final CustomObservableList<ObservableString> toolProficiencies = new CustomObservableList<>();
    private final CustomObservableList<ObservableString> selectableFeats = new CustomObservableList<>();
    private final CustomObservableList<Proficiency> choiceProficiencies = new CustomObservableList<>();
    private final CustomObservableList<Spell> availableCantrips = new CustomObservableList<>();
    private final CustomObservableList<Spell> availableSpells = new CustomObservableList<>();
    private final CustomObservableList<Spell> spells = new CustomObservableList<>();
    private final CustomObservableList<Spell> cantrips = new CustomObservableList<>();

    // Instead of this I could turn availableSubclasses and availableLineages into Lists, but it shouldn't change much
    private final int maxSubclasses;
    private final int maxLineages;
    private final int maxFeats;
    private final int maxSets;

    private int maxSkills;
    private final int[] skillAbilities;

    private final ObservableInteger level = new ObservableInteger(0);
    private final ObservableInteger initiativeBonus = new ObservableInteger(0);
    private final ObservableInteger proficiencyBonus = new ObservableInteger(2);
    private final ObservableInteger speed = new ObservableInteger(30);
    private final ObservableInteger darkvision = new ObservableInteger(60);
    private final ObservableInteger armorClass = new ObservableInteger(10);
    private final ObservableInteger health = new ObservableInteger(1);
    private final ObservableInteger fixedHealth = new ObservableInteger(0);
    private final ObservableInteger hitDie = new ObservableInteger(0);
    private final ObservableInteger givenBonuses = new ObservableInteger(0);
    private final ObservableInteger givenSkills = new ObservableInteger(0);
    private final ObservableInteger generationPoints = new ObservableInteger(0);
    private final ObservableInteger availableFeats = new ObservableInteger(0);
    private final ObservableInteger exhaustion = new ObservableInteger(0);
    private final ObservableInteger maxCantrips = new ObservableInteger(0);
    private final ObservableInteger maxSpells = new ObservableInteger(0);
    private final ObservableInteger spellcastingAbilityModifier = new ObservableInteger(0);
    private final ObservableInteger spellcastingAttackModifier = new ObservableInteger(0);
    private final ObservableInteger spellcastingSaveDC = new ObservableInteger(8);
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
        skillNames = getGroup(new String[]{"skills"});
        abilityNames = getGroup(new String[]{"abilities"});
        sets = getGroup(new String[]{"sets"});
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

        bindLevel();
        bindProficiencyBonus();

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

            bindAbilityBase(i);

            bindAvailablePlusOne(i);
            bindAvailablePlusTwo(i);

            bindAvailablePluses(i);
            bindAvailableMinuses(i);
        }

        maxFeats = getInt(new String[]{"max_feats"});
        feats = new ObservableString[maxFeats];
        featOnes = new ObservableString[maxFeats];
        featTwos = new ObservableString[maxFeats];
        featsAbilities = new ObservableString[maxFeats][abilityCount];
        for (int i = 0; i < feats.length; i++) {
            feats[i] = new ObservableString("RANDOM");
            featOnes[i] = new ObservableString("NONE_M");
            featTwos[i] = new ObservableString("NONE_M");
            for (int j = 0; j < abilityCount; j++) {
                featsAbilities[i][j] = new ObservableString("");
            }
        }

        bindFeatsAbilities();

        maxSets = getInt(new String[]{"max_sets"});
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

        maxSubclasses = getInt(new String[]{"max_subclasses"});
        availableSubclasses = new ObservableString[maxSubclasses];
        bindAvailableSubclasses();

        for (int i = 0; i < abilityBases.length; i++) {
            bindSavingThrowProficiencies(i);

            bindSavingThrowBonus(i);
            bindSavingThrowModifier(i);
        }

        for (int i = 0; i < skillBonuses.length; i++) {
            skillAbilities[i] = getInt(new String[]{"skills", skillNames[i], "ability"});
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

        maxLineages = getInt(new String[]{"max_lineages"});
        availableLineages = new ObservableString[maxLineages];
        bindAvailableLineages();

        bindGenerationPoints();
        bindGenerationMethod();

        bindSpeed();
        bindDarkvision();
        bindArmorClass();
        bindInitiativeBonus();

        bindHitDie();
        bindHealth();
        bindCreatureType();

        availableSizes = new ObservableString[2];
        bindAvailableSizes();

        bindAvailableFeats();
        bindFeatOnesTwos();

        bindOriginFeat();

        bindActives();

        bindPassives();

        bindChoiceProficiencies();

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
    }

    // Getters
    public String[] getSkillNames() {
        return skillNames;
    }

    public String[] getAbilityNames() {
        return abilityNames;
    }

    public ObservableString getAvailableSize(int index) {
        return availableSizes[index];
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

    public ObservableString getSubclass() {
        return subclass;
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

    public ObservableString getSpellcastingAbility() {
        return spellcastingAbility;
    }

    public ObservableInteger getSpellcastingAbilityModifier() {
        return spellcastingAbilityModifier;
    }

    public ObservableInteger getSpellcastingAttackModifier() {
        return spellcastingAttackModifier;
    }

    public ObservableInteger getSpellcastingSaveDC() {
        return spellcastingSaveDC;
    }

    public ObservableString getOriginFeat() {
        return originFeat;
    }

    public ObservableString getLevelShown() {
        return levelShown;
    }

    public ObservableInteger getLevel() {
        return level;
    }

    public ObservableString getClasse() {
        return classe;
    }

    public ObservableString getSpecies() {
        return species;
    }

    public ObservableString getBackground() {
        return background;
    }

    public ObservableString getAvailableSubclass(int index) {
        return availableSubclasses[index];
    }

    public ObservableString getAvailableLineage(int index) {
        return availableLineages[index];
    }

    public ObservableString getAbilityBasesShown(int index) {
        return abilityBasesShown[index];
    }

    public ObservableString getFeat(int index) {
        return feats[index];
    }

    public ObservableString getFeatOne(int index) {
        return featOnes[index];
    }

    public ObservableString getFeatTwo(int index) {
        return featTwos[index];
    }

    public ObservableString getFeatAbility(int featIndex, int abilityIndex) {
        return featsAbilities[featIndex][abilityIndex];
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

    public ObservableInteger getAvailableFeats() {
        return availableFeats;
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

    public ObservableInteger getFixedHealth() {
        return fixedHealth;
    }

    public ObservableInteger getHitDie() {
        return hitDie;
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

    public ObservableInteger getAvailableSpellSlot(int index) {
        return availableSpellSlots[index];
    }

    public ObservableInteger getMaxCantrips() {
        return maxCantrips;
    }

    public ObservableInteger getMaxSpells() {
        return maxSpells;
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

    public CustomObservableList<ObservableString> getActives() {
        return actives;
    }

    public CustomObservableList<ObservableString> getPassives() {
        return passives;
    }

    public CustomObservableList<ObservableString> getWeaponProficiencies() {
        return weaponProficiencies;
    }

    public CustomObservableList<ObservableString> getArmorProficiencies() {
        return armorProficiencies;
    }

    public CustomObservableList<ObservableString> getToolProficiencies() {
        return toolProficiencies;
    }

    public ObservableString getClassEquipment(int index) {
        return classEquipment[index];
    }

    public ObservableString getBackgroundEquipment(int index) {
        return backgroundEquipment[index];
    }

    public CustomObservableList<Proficiency> getChoiceProficiencies() {
        return choiceProficiencies;
    }

    public CustomObservableList<ObservableString> getNewSelectableFeats() {
        return selectableFeats;
    }

    public CustomObservableList<Spell> getAvailableCantrips() {
        return availableCantrips;
    }

    public CustomObservableList<Spell> getAvailableSpells() {
        return availableSpells;
    }

    public CustomObservableList<Spell> getSpells() {
        return spells;
    }

    public CustomObservableList<Spell> getCantrips() {
        return cantrips;
    }

    // Binders
    private void bindAvailableSubclasses() {
        // Listen for changes to the 'classe' property and update availableSubclasses accordingly
        Runnable updateSubclasses = () -> {
            if (level.get() >= 3) {
                String[] subclasses = getGroup(new String[]{"classes", classe.get(), "subclasses"});
                for (int i = 0; i < availableSubclasses.length; i++) {
                    if (subclasses != null && i < subclasses.length) {
                        availableSubclasses[i].set(subclasses[i]);
                    } else {
                        availableSubclasses[i].set("");
                    }
                }
            } else {
                // If level < 3, clear all subclasses
                for (ObservableString availableSubclass : availableSubclasses) {
                    availableSubclass.set("");
                }
            }
        };

        classe.addListener(_ -> updateSubclasses.run());
        level.addListener(_ -> updateSubclasses.run());

        for (int i = 0; i < availableSubclasses.length; i++) {
            availableSubclasses[i] = new ObservableString("");
        }

        // Initial population
        updateSubclasses.run();
    }

    private void bindOriginFeat() {
        background.addListener(
                (newVal) -> {
                    originFeat.set(getString(new String[]{"backgrounds", newVal, "feat"}));
                }
        );
    }

    private void bindAvailableSizes() {
        species.addListener(
                (newVal) -> {
                    String[] sizes = getGroup(new String[]{"species", newVal, "size"});
                    for (int i = 0; i < availableSizes.length; i++) {
                        if (sizes != null && i < sizes.length) {
                            availableSizes[i].set(sizes[i]);
                        } else {
                            availableSizes[i].set("");
                        }
                    }
                    if (availableSizes[1].get().equals("")) {
                        size.set(sizes != null && sizes.length > 0 ? sizes[0] : "");
                    } else if (sizes != null && !Arrays.asList(sizes).contains(size.get())) {
                        size.set("RANDOM");
                    }
                }
        );

        // Initialize availableSizes with empty strings
        for (int i = 0; i < availableSizes.length; i++) {
            availableSizes[i] = new ObservableString("");
        }
    }

    private void bindAvailableFeats() {
        Runnable updateAvailableFeats = () -> {
            int[] possibleFeats = getInts(new String[]{"classes", classe.get(), "feats"});
            int i = 0;
            if (possibleFeats != null) {
                for (int possibleFeat : possibleFeats) {
                    if (possibleFeat <= level.get()) {
                        i++;
                    } else {
                        availableFeats.set(i);
                        return;
                    }
                }
            }
            availableFeats.set(i);
        };

        classe.addListener(_ -> updateAvailableFeats.run());
        level.addListener(_ -> updateAvailableFeats.run());

        availableFeats.addListener(
                (newVal) -> {
                    for (int i = newVal; i < feats.length; i++) {
                        feats[i].set("RANDOM");
                    }
                }
        );
    }

    private void bindFeatOnesTwos() {
        for (int i = 0; i < feats.length; i++) {
            int index = i;
            feats[index].addListener(
                    (newVal) -> {
                        String[] abilitiesPossible = getGroup(new String[]{"feats", newVal, "abilities"});
                        String randomValue = "RANDOM";
                        if (abilitiesPossible.length == 1) {
                            randomValue = abilitiesPossible[0];
                        } else if (abilitiesPossible.length == 0) {
                            randomValue = "NONE_M";
                        }
                        int n = getInt(new String[]{"feats", newVal, "max"});
                        switch (n) {
                            case 2 -> {
                                featTwos[index].set(randomValue);
                                featOnes[index].set(randomValue);
                            }
                            case 1 -> {
                                featTwos[index].set("NONE_M");
                                featOnes[index].set(randomValue);
                            }
                            default -> {
                                featTwos[index].set("NONE_M");
                                featOnes[index].set("NONE_M");
                            }
                        }
                    });
        }
    }

    private void bindAvailableLineages() {
        species.addListener(
                (newVal) -> {
                    String[] lineages = getGroup(new String[]{"species", newVal, "lineages"});
                    for (int i = 0; i < availableLineages.length; i++) {
                        if (lineages != null && i < lineages.length) {
                            availableLineages[i].set(lineages[i]);
                        } else {
                            availableLineages[i].set("");
                        }
                    }
                }
        );

        for (int i = 0; i < availableLineages.length; i++) {
            availableLineages[i] = new ObservableString("");
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
                    creatureType.set(getString(new String[]{"species", newVal, "type"}));
                }
        );
    }

    private void bindAvailablePlusOne(int index) {
        Runnable updateAvailablePlusOne = () -> {
            Boolean bool = false;
            String[] possibleAbilities = getGroup(new String[]{"backgrounds", background.get(), "abilities"});
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
                }
        );
    }

    private void bindAvailablePlusTwo(int index) {
        Runnable updateAvailablePlusTwo = () -> {
            Boolean bool = false;
            String[] possibleAbilities = getGroup(new String[]{"backgrounds", background.get(), "abilities"});
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
                }
        );
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
            String[] possibleSkills = getGroup(new String[]{"classes", classe.get(), "skills"});
            maxSkills = getInt(new String[]{"classes", classe.get(), "skills_number"});
            if (Arrays.asList(possibleSkills).contains(skillNames[index])
                    && !fixedSkills[index].get()
                    && (givenSkills.get() < maxSkills || skillProficiencies[index].get())) {
                availableSkills[index].set(true);
            } else {
                availableSkills[index].set(false);
            }
        };
        classe.addListener(_ -> updateAvailableSkills.run());
        givenSkills.addListener(_ -> updateAvailableSkills.run());
        fixedSkills[index].addListener(_ -> updateAvailableSkills.run());

        availableSkills[index].addListener(
                (newVal) -> {
                    if (!newVal && skillProficiencies[index].get() && !fixedSkills[index].get()) {
                        skillProficiencies[index].set(false);
                    }
                }
        );
    }

    private void bindFixedSkills() {
        background.addListener(
                (newVal) -> {
                    String[] possibleSkills = getGroup(new String[]{"backgrounds", newVal, "skills"});
                    for (int i = 0; i < fixedSkills.length; i++) {
                        fixedSkills[i].set(java.util.Arrays.asList(possibleSkills).contains(skillNames[i]));
                    }
                }
        );

        for (int i = 0; i < fixedSkills.length; i++) {
            fixedSkills[i] = new ObservableBoolean(false);
        }
    }

    private void bindFinalAbility(int index) {
        // Bind finalAbilities to track (abilityBases + bonuses), defaulting to 10 if abilityBases[index] is 0
        Runnable updateFinalAbility = () -> {
            int base = abilityBases[index].get(); // ObservableInteger
            int one = abilityPlusOnes[index].get() ? 1 : 0;
            int two = abilityPlusTwos[index].get() ? 2 : 0;
            int featOne = 0;
            int featTwo = 0;
            int boonOne = 0;
            int boonTwo = 0;

            for (int i = 0; i < feats.length; i++) {
                String featName = featOnes[i].get();
                if (featName != null && featName.equals(abilityNames[index])) {
                    if (getString(new String[]{"feats", feats[i].get(), "type"}).equals("EPIC_BOON")) {
                        boonOne++;
                    } else {
                        featOne++;
                    }
                }
            }

            for (int i = 0; i < feats.length; i++) {
                String featName = featTwos[i].get();
                if (featName != null && featName.equals(abilityNames[index])) {
                    if (getString(new String[]{"feats", feats[i].get(), "type"}).equals("EPIC_BOON")) {
                        boonTwo++;
                    } else {
                        featTwo++;
                    }
                }
            }

            abilities[index].set(Math.min(Math.min((base != 0 ? base : 10) + one + two + featOne + featTwo, 20) + boonOne + boonTwo, 30));
        };
        abilityBases[index].addListener(_ -> updateFinalAbility.run());
        abilityPlusOnes[index].addListener(_ -> updateFinalAbility.run());
        abilityPlusTwos[index].addListener(_ -> updateFinalAbility.run());

        for (int i = 0; i < feats.length; i++) {
            featOnes[i].addListener(_ -> updateFinalAbility.run());
            featTwos[i].addListener(_ -> updateFinalAbility.run());
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
            String[] possibleSaves = getGroup(new String[]{"classes", classe.get(), "abilities"});
            savingThrowProficiencies[index].set(Arrays.asList(possibleSaves).contains(abilityNames[index]));
        };
        classe.addListener(_ -> updateSavingThrowProficiencies.run());
    }

    private void bindSavingThrowBonus(int index) {
        // Bind savingThrowBonuses[index] to include proficiencyBonus if savingThrowProficiencies[index] is true
        // Uselessely convoluted, but it refused to believe that proficiencyBonus can't be null. I don't like seeing warnings, so I did this.
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
                        //reset all abilities to 8
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
                }
        );
    }

    private void bindSavingThrowModifier(int index) {
        // Bind the abilityModifier to the corresponding ability
        Runnable updateSavingThrowModifier = () -> {
            savingThrowModifiers[index].set(
                    abilityModifiers[index].get() + savingThrowBonuses[index].get()
            );
        };
        abilityModifiers[index].addListener(_ -> updateSavingThrowModifier.run());
        savingThrowBonuses[index].addListener(_ -> updateSavingThrowModifier.run());
    }

    private void bindSkillModifier(int index) {
        // Bind the skillModifier to the corresponding ability
        Runnable updateSkillModifier = () -> {
            skillModifiers[index].set(
                    abilityModifiers[skillAbilities[index]].get() + skillBonuses[index].get()
            );
        };
        abilityModifiers[skillAbilities[index]].addListener(_ -> updateSkillModifier.run());
        skillBonuses[index].addListener(_ -> updateSkillModifier.run());
    }

    private void bindSkillBonus(int index) {
        Runnable updateSkillBonus = () -> {
            Boolean isProficient = skillProficiencies[index].get();
            Integer profBonus = proficiencyBonus.get();
            skillBonuses[index].set(
                    (isProficient != null && isProficient && profBonus != null) ? profBonus : 0
            );
        };
        skillProficiencies[index].addListener(_ -> updateSkillBonus.run());
        proficiencyBonus.addListener(_ -> updateSkillBonus.run());
    }

    private void bindSkillProficiency(int index) {
        // Bind the skillModifier to the corresponding ability
        skillProficiencies[index] = new ObservableBoolean(false);

        fixedSkills[index].addListener(
                (newVal) -> {
                    if (newVal) {
                        skillProficiencies[index].set(newVal);
                    }
                }
        );
    }

    private void bindSpeed() {
        Runnable updateSpeed = () -> {
            int baseSpeed = getInt(new String[]{"species", species.get(), "speed"});
            if (baseSpeed > 0) {
                speed.set(baseSpeed);
            } else {
                speed.set(30);
            }
            if (grappled.get() || paralyzed.get() || petrified.get() || restrained.get() || unconscious.get()) {
                speed.set(0);
            } else {
                speed.set(Math.max(speed.get() - exhaustion.get() * 5, 0));
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
    }

    private void bindDarkvision() {
        Runnable updateDarkvision = () -> {
            int baseDarkvision = getInt(new String[]{"species", species.get(), "darkvision"});
            darkvision.set(baseDarkvision);
        };
        species.addListener(_ -> updateDarkvision.run());
        lineage.addListener(_ -> updateDarkvision.run());
    }

    private void bindArmorClass() {
        Runnable updateArmorClass = () -> {
            armorClass.set(
                    abilityModifiers[1].get() + 10
            );
        };
        abilityModifiers[1].addListener(_ -> updateArmorClass.run());
    }

    private void bindProficiencyBonus() {
        Runnable updateProficiencyBonus = () -> {
            proficiencyBonus.set(
                    (level.get() + 7) / 4
            );
        };
        level.addListener(_ -> updateProficiencyBonus.run());
    }

    private void bindInitiativeBonus() {
        Runnable updateInitiativeBonus = () -> {
            initiativeBonus.set(
                    abilityModifiers[1].get()
            );
        };
        abilityModifiers[1].addListener(_ -> updateInitiativeBonus.run());
    }

    private void bindHealth() {
        Runnable updateHealth = () -> {
            fixedHealth.set(Math.max(hitDie.get() + abilityModifiers[2].get() + (level.get() - 1) * abilityModifiers[2].get(), 1));
            if (healthMethod.get().equals("MEDIUM_HP")) {
                health.set(Math.max((fixedHealth.get() + (level.get() - 1) * (hitDie.get() / 2 + 1)), 1));
            } else if (healthMethod.get().equals("RANDOM")) {
                health.set(0);
            }
        };
        level.addListener(_ -> updateHealth.run());
        hitDie.addListener(_ -> updateHealth.run());
        healthMethod.addListener(_ -> updateHealth.run());
        abilityModifiers[2].addListener(_ -> updateHealth.run());
    }

    private void bindHitDie() {
        Runnable updateHitDie = () -> {
            int baseHitDie = getInt(new String[]{"classes", classe.get(), "hit_die"});
            hitDie.set(baseHitDie > 0 ? baseHitDie : 4);
        };
        classe.addListener(_ -> updateHitDie.run());
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
        levelShown.addListener((newValue) -> {
            if (newValue != null) {
                if (newValue.equals("RANDOM")) {
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

        // Update `levelShown` when `level` changes
        level.addListener((newValue) -> {
            if (newValue != null) {
                if (newValue == 0) {
                    // If level is 0, set `levelShown` to "RANDOM"
                    levelShown.set("RANDOM");
                } else {
                    // Otherwise, set `levelShown` to the string representation of the level
                    levelShown.set(String.valueOf(newValue));
                }
            }
        });
    }

    private void bindAbilityBase(int index) {
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

    private void bindActives() {
        Runnable updateActives = () -> {
            actives.clear(); // Keep the list in sync
            String[] activeNames = getGroup(new String[]{"species", species.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    if (level.get() >= getInt(new String[]{"species", species.get(), "actives", active})) {
                        actives.add(new ObservableString(active));
                    }
                }
            }

            activeNames = getGroup(new String[]{"species", species.get(), "lineages", lineage.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    if (level.get() >= getInt(new String[]{"species", species.get(), "lineages", lineage.get(), "actives", active})) {
                        actives.add(new ObservableString(active));
                    }
                }
            }

            activeNames = getGroup(new String[]{"classes", classe.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    if (level.get() >= getInt(new String[]{"classes", classe.get(), "actives", active})) {
                        actives.add(new ObservableString(active));
                    }
                }
            }

            activeNames = getGroup(new String[]{"classes", classe.get(), "subclasses", subclass.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    if (level.get() >= getInt(new String[]{"classes", classe.get(), "subclasses", subclass.get(), "actives", active})) {
                        actives.add(new ObservableString(active));
                    }
                }
            }

            activeNames = getGroup(new String[]{"feats", originFeat.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    actives.add(new ObservableString(active));
                }
            }

            for (ObservableString feat : feats) {
                activeNames = getGroup(new String[]{"feats", feat.get(), "actives"});
                if (activeNames != null) {
                    for (String active : activeNames) {
                        if (level.get() >= getInt(new String[]{"feats", feat.get(), "actives", active})) {
                            actives.add(new ObservableString(active));
                        }
                    }
                }
            }
        };

        species.addListener(_ -> updateActives.run());
        lineage.addListener(_ -> updateActives.run());
        level.addListener(_ -> updateActives.run());
        originFeat.addListener(_ -> updateActives.run());
        for (ObservableString feat : feats) {
            feat.addListener(_ -> updateActives.run());
        }
        classe.addListener(_ -> updateActives.run());
        subclass.addListener(_ -> updateActives.run());
        updateActives.run();
    }

    private void bindFeatsAbilities() {
        for (int i = 0; i < feats.length; i++) {
            int index = i;
            Runnable updateFeatAbilities = () -> {
                String[] featAbilities = getGroup(new String[]{"feats", feats[index].get(), "abilities"});
                for (int j = 0; j < featsAbilities[index].length; j++) {
                    if (j < featAbilities.length) {
                        featsAbilities[index][j].set(featAbilities[j]);
                    } else {
                        featsAbilities[index][j].set("");
                    }
                }
            };
            feats[i].addListener(_ -> updateFeatAbilities.run());
            updateFeatAbilities.run();
        }
    }

    private void bindPassives() {
        Runnable updatePassives = () -> {
            passives.clear(); // Keep the list in sync
            String[] passiveNames = getGroup(new String[]{"species", species.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[]{"species", species.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }

            passiveNames = getGroup(new String[]{"species", species.get(), "lineages", lineage.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[]{"species", species.get(), "lineages", lineage.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }

            passiveNames = getGroup(new String[]{"classes", classe.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[]{"classes", classe.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }

            passiveNames = getGroup(new String[]{"classes", classe.get(), "subclasses", subclass.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[]{"classes", classe.get(), "subclasses", subclass.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }

            passiveNames = getGroup(new String[]{"feats", originFeat.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[]{"feats", originFeat.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }

            for (ObservableString feat : feats) {
                passiveNames = getGroup(new String[]{"feats", feat.get(), "passives"});
                if (passiveNames != null) {
                    for (String passive : passiveNames) {
                        if (level.get() >= getInt(new String[]{"feats", feat.get(), "passives", passive})) {
                            passives.add(new ObservableString(passive));
                        }
                    }
                }
            }
        };

        species.addListener(_ -> updatePassives.run());
        lineage.addListener(_ -> updatePassives.run());
        level.addListener(_ -> updatePassives.run());
        originFeat.addListener(_ -> updatePassives.run());
        for (ObservableString feat : feats) {
            feat.addListener(_ -> updatePassives.run());
        }
        classe.addListener(_ -> updatePassives.run());
        subclass.addListener(_ -> updatePassives.run());
        updatePassives.run();
    }

    private void bindWeaponProficiencies() {
        Runnable updateWeaponProficiencies = () -> {
            weaponProficiencies.clear();

            String[] weapons = getGroup(new String[]{"classes", classe.get(), "weapons"});
            if (weapons != null) {
                for (String weapon : weapons) {
                    if (weapon != null) {
                        weaponProficiencies.add(new ObservableString(weapon));
                    }
                }
            }
        };

        updateWeaponProficiencies.run();
        classe.addListener(_ -> updateWeaponProficiencies.run());
    }

    private void bindArmorProficiencies() {
        Runnable updateArmorProficiencies = () -> {
            armorProficiencies.clear();

            String[] armors = getGroup(new String[]{"classes", classe.get(), "armors"});
            if (armors != null) {
                for (String armor : armors) {
                    if (armor != null) {
                        armorProficiencies.add(new ObservableString(armor));
                    }
                }
            }
        };

        updateArmorProficiencies.run();
        classe.addListener(_ -> updateArmorProficiencies.run());
    }

    private void bindToolProficiencies() {
        Runnable updateToolProficiencies = () -> {
            List<ObservableString> newTools = new ArrayList<>();
            // in the 2024 rules only one tool proficiency is given for each background. Done like this for future compatibility
            String[] tools = getGroup(new String[]{"backgrounds", background.get(), "tools"});
            if (tools != null) {
                for (String tool : tools) {
                    if (tool != null) {
                        newTools.add(new ObservableString(tool));
                    }
                }
            }

            tools = getGroup(new String[]{"classes", classe.get(), "tools"});
            if (tools != null) {
                for (String tool : tools) {
                    if (tool != null) {
                        newTools.add(new ObservableString(tool));
                    }
                }
            }

            toolProficiencies.setAll(newTools); // Only triggers listeners once
        };

        background.addListener(_ -> updateToolProficiencies.run());
        classe.addListener(_ -> updateToolProficiencies.run());
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
        Runnable updateSelectableFeats = () -> {
            List<ObservableString> newFeats = new ArrayList<>();
            String[] selectableFeatsTotal = getSelectableFeats();
            for (String feat : selectableFeatsTotal) {
                int[] stats = getInts(new String[]{"feats", feat, "stats"});
                Boolean magic = getBoolean(new String[]{"feats", feat, "magic"});
                String[] proficienciesArmor = getGroup(new String[]{"feats", feat, "armorProficiencies"});
                String type = getString(new String[]{"feats", feat, "type"});
                Boolean valid = true;

                Boolean found;
                for (String proficiency : proficienciesArmor) {
                    found = false;
                    for (ObservableString obsStr : armorProficiencies.asList()) {
                        if (obsStr != null && proficiency.equals(obsStr.get())) {
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

                if (magic && !Arrays.asList(getMagicClasses()).contains(classe.get())) {
                    valid = false;
                }

                Boolean epicBoon = false;
                for (ObservableString actualFeat : feats) {
                    if (getString(new String[]{"feats", actualFeat.get(), "type"}).equals("EPIC_BOON") && !actualFeat.get().equals(feat)) {
                        epicBoon = true;
                        break;
                    }
                }

                if (!feat.equals(originFeat.get())
                        && (valid || type.equals("ORIGIN"))
                        && level.get() >= getInt(new String[]{"feats", feat, "level"})
                        && (!type.equals("EPIC_BOON") || !epicBoon)) {
                    newFeats.add(new ObservableString(feat));
                }
            }
            selectableFeats.setAll(newFeats); // Only triggers listeners once
        };
        originFeat.addListener(_ -> updateSelectableFeats.run());
        level.addListener(_ -> updateSelectableFeats.run());
        classe.addListener(_ -> updateSelectableFeats.run());
        for (ObservableInteger ability : abilities) {
            ability.addListener(_ -> updateSelectableFeats.run());
        }
        updateSelectableFeats.run();
    }

    private void bindClassEquipment() {
        classe.addListener((newVal) -> {
            classEquipment[0].set(newVal);

            String[] equipments = getGroup(new String[]{"classes", newVal, "equipment"});

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

            String[] equipments = getGroup(new String[]{"backgrounds", newVal, "equipment"});

            for (int i = 1; i < backgroundEquipment.length; i++) {
                if (i - 1 < equipments.length) {
                    backgroundEquipment[i].set("RANDOM");
                } else {
                    backgroundEquipment[i].set("");
                }
            }
        });
    }

    private void bindChoiceProficiencies() {
        toolProficiencies.addListener((newVal) -> {
            List<Proficiency> newChoices = new ArrayList<>();
            for (ObservableString equipment : newVal.asList()) {
                if (Arrays.asList(sets).contains(equipment.get())) {
                    newChoices.add(new Proficiency("RANDOM", equipment.get()));
                }
            }

            for (Proficiency choice : choiceProficiencies.asList()) {
                for (Proficiency newChoice : newChoices) {
                    if (newChoice.getGroup().equals(choice.getGroup()) && newChoice.getName().equals("RANDOM")) {
                        newChoice.setName(choice.getName());
                    }
                }
            }

            choiceProficiencies.setAll(newChoices);
        });
    }

    private void bindSpells() {
        Runnable updateSpells = () -> {
            availableSpells.clear();
            availableCantrips.clear();
            if ((Arrays.asList(getMagicClasses()).contains(classe.get())) && level.get() > 0) {
                int[] slots = getInts(new String[]{"spell_slots", String.valueOf(level.get())});
                int maximumLevel = 0;
                for (int i = 0; i < spellSlots.length; i++) {
                    int oldSlots = spellSlots[i].get();
                    if (slots[i] > oldSlots && availableSpellSlots[i].get() < slots[i]) {
                        availableSpellSlots[i].set(availableSpellSlots[i].get() + slots[i] - oldSlots);
                    }
                    spellSlots[i].set(slots[i]);
                    if (slots[i] > 0) {
                        maximumLevel++;
                    }
                }

                String[] spellsList = getTotalSpells();
                for (String spell : spellsList) {
                    int spellLevel = getSpellInt(new String[]{spell, "level"});
                    String[] acceptedClasses = getSpellGroup(new String[]{spell, "lists"});

                    if (spellLevel <= maximumLevel && Arrays.asList(acceptedClasses).contains(classe.get())) {
                        if (spellLevel > 0) {
                            availableSpells.add(new Spell(spell, getString(new String[]{classe.get(), "change"}), new String[0], Arrays.asList(abilityNames).indexOf(spellcastingAbility.get()), false, getBoolean(new String[]{classe.get(), "limited"})));
                        } else if (level.get() >= 1) {
                            availableCantrips.add(new Spell(spell, getString(new String[]{classe.get(), "change"}), new String[0], Arrays.asList(abilityNames).indexOf(spellcastingAbility.get()), false, getBoolean(new String[]{classe.get(), "limited"})));
                        }
                    }
                }
            } else {
                for (ObservableInteger spellSlot : spellSlots) {
                    spellSlot.set(0);
                }
            }
        };
        classe.addListener(_ -> updateSpells.run());
        level.addListener(_ -> updateSpells.run());
    }

    private void bindMaxSpells() {
        Runnable updateMaxSpells = () -> {
            int[] spellsPerLevel = (getInts(new String[]{"classes", classe.get(), "prepared"}));
            int[] cantripsPerLevel = (getInts(new String[]{"classes", classe.get(), "cantrips"}));
            if (level.get() > 0) {
                if (spellsPerLevel.length > 0) {
                    maxSpells.set(spellsPerLevel[level.get() - 1]);
                } else {
                    maxSpells.set(0);
                }
                if (cantripsPerLevel.length > 0) {
                    maxCantrips.set(cantripsPerLevel[level.get() - 1]);
                } else {
                    maxCantrips.set(0);
                }
            } else {
                maxSpells.set(0);
                maxCantrips.set(0);
            }
        };
        classe.addListener(_ -> updateMaxSpells.run());
        level.addListener(_ -> updateMaxSpells.run());
    }

    private void bindSpellcastingAbility() {
        classe.addListener((newVal) -> {
            spellcastingAbility.set(getString(new String[]{"classes", newVal, "spellcasting"}));
        });

        Runnable updateSpellcastingAbility = () -> {
            for (int i = 0; i < abilityNames.length; i++) {
                if (abilityNames[i].equals(spellcastingAbility.get())) {
                    spellcastingAbilityModifier.set(abilityModifiers[i].get());
                    spellcastingAttackModifier.set(abilityModifiers[i].get() + proficiencyBonus.get());
                    spellcastingSaveDC.set(8 + spellcastingAttackModifier.get());
                    break;
                }
            }
        };
        spellcastingAbility.addListener(_ -> updateSpellcastingAbility.run());
        proficiencyBonus.addListener(_ -> updateSpellcastingAbility.run());
        for (int i = 0; i < abilityNames.length; i++) {
            abilityModifiers[i].addListener(_ -> updateSpellcastingAbility.run());
        }
    }

    private void bindSubclass() {
        classe.addListener(_ -> {
            subclass.set("RANDOM");
        });
    }

    // Helpers
    private String[] getGroup(String[] group) {
        return TranslationManager.getInstance().getGroup(group);
    }

    private String getString(String[] group) {
        return TranslationManager.getInstance().getString(group);
    }

    private int getInt(String[] group) {
        return TranslationManager.getInstance().getInt(group);
    }

    private int[] getInts(String[] group) {
        return TranslationManager.getInstance().getInts(group);
    }

    private String[] getSelectableFeats() {
        return TranslationManager.getInstance().getSelectableFeats();
    }

    private Boolean getBoolean(String[] group) {
        return TranslationManager.getInstance().getBoolean(group);
    }

    private String[] getMagicClasses() {
        return TranslationManager.getInstance().getMagicClasses();
    }

    private String[] getTotalSpells() {
        return SpellManager.getInstance().getTotalSpells();
    }

    private int getSpellInt(String[] group) {
        return SpellManager.getInstance().getSpellInt(group);
    }

    private String[] getSpellGroup(String[] group) {
        return SpellManager.getInstance().getSpellGroup(group);
    }

    public GameCharacter duplicate() {
        GameCharacter copy = new GameCharacter();
        copy.gender.set(this.gender.get());
        copy.languageOne.set(this.languageOne.get());
        copy.languageTwo.set(this.languageTwo.get());
        copy.classe.set(this.classe.get());
        copy.species.set(this.species.get());
        copy.background.set(this.background.get());
        copy.levelShown.set(this.levelShown.get());
        copy.alignment.set(this.alignment.get());
        copy.subclass.set(this.subclass.get());
        copy.lineage.set(this.lineage.get());
        copy.size.set(this.size.get());
        copy.name.set(this.name.get());
        for (int i = 0; i < feats.length; i++) {
            copy.feats[i].set(this.feats[i].get());
            copy.featOnes[i].set(this.featOnes[i].get());
            copy.featTwos[i].set(this.featTwos[i].get());
        }
        for (int i = 0; i < classEquipment.length; i++) {
            copy.classEquipment[i].set(this.classEquipment[i].get());
            copy.backgroundEquipment[i].set(this.backgroundEquipment[i].get());
        }
        copy.choiceProficiencies.setAll(choiceProficiencies.asList());
        copy.spells.setAll(spells.asList());
        copy.cantrips.setAll(cantrips.asList());
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
        
        return copy;
    }

    public void fill() {
        if (gender.get().equals("RANDOM")) {
            String[] availableGenders = getGroup(new String[]{"genders"});
            gender.set(availableGenders[(int) (Math.random() * availableGenders.length)]);
        }

        if (languageOne.get().equals("RANDOM")) {
            String[] availableLanguages = getGroup(new String[]{"common_languages"});
            List<String> langList = new ArrayList<>(Arrays.asList(availableLanguages));
            if (!languageTwo.get().equals("RANDOM")) {
                langList.remove(languageTwo.get());
            }
            languageOne.set(langList.get((int) (Math.random() * langList.size())));
        }

        if (languageTwo.get().equals("RANDOM")) {
            String[] availableLanguages = getGroup(new String[]{"common_languages"});
            List<String> langList = new ArrayList<>(Arrays.asList(availableLanguages));
            if (!languageOne.get().equals("RANDOM")) {
                langList.remove(languageOne.get());
            }
            languageTwo.set(langList.get((int) (Math.random() * langList.size())));
        }

        if (classe.get().equals("RANDOM")) {
            String[] availableClasses = getGroup(new String[]{"classes"});
            classe.set(availableClasses[(int) (Math.random() * availableClasses.length)]);
        }

        if (species.get().equals("RANDOM")) {
            String[] availableSpecies = getGroup(new String[]{"species"});
            species.set(availableSpecies[(int) (Math.random() * availableSpecies.length)]);
        }

        if (background.get().equals("RANDOM")) {
            String[] availableBackgrounds = getGroup(new String[]{"backgrounds"});
            background.set(availableBackgrounds[(int) (Math.random() * availableBackgrounds.length)]);
        }

        if (alignment.get().equals("RANDOM")) {
            String[] availableAlignments = getGroup(new String[]{"alignments"});
            alignment.set(availableAlignments[(int) (Math.random() * availableAlignments.length)]);
        }

        fillLater();
    }

    public void fillLater() {
        if (levelShown.get().equals("RANDOM")) {
            int randomLevel = (int) (Math.random() * 20) + 1;
            levelShown.set(String.valueOf(randomLevel));
        }

        if (subclass.get().equals("RANDOM")) {
            // Filter out empty subclasses
            List<String> validSubclasses = new ArrayList<>();
            for (ObservableString subclassOption : availableSubclasses) {
                if (!subclassOption.get().isEmpty()) {
                    validSubclasses.add(subclassOption.get());
                }
            }
            if (!validSubclasses.isEmpty()) {
                subclass.set(validSubclasses.get((int) (Math.random() * validSubclasses.size())));
            }
        }
        
        if (lineage.get().equals("RANDOM")) {
            // Filter out empty lineages
            List<String> validLineages = new ArrayList<>();
            for (ObservableString lineageOption : availableLineages) {
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
            for (ObservableString sizeOption : availableSizes) {
                if (!sizeOption.get().isEmpty()) {
                    validSizes.add(sizeOption.get());
                }
            }
            if (!validSizes.isEmpty()) {
                size.set(validSizes.get((int) (Math.random() * validSizes.size())));
            }
        }

        // Remove selected non repeatable feats
        List<ObservableString> possibleFeats = selectableFeats.asList();for (int i = 0; i < feats.length; i++) {
            int index = i;
            if (!feats[i].get().equals("RANDOM") && availableFeats.get() > i) {
                if (!getBoolean(new String[]{"feats", feats[i].get(), "repeatable"})) {
                    possibleFeats.removeIf(feat -> feat.get().equals(feats[index].get()));
                }
            } else if (availableFeats.get() <= i) {
                break;
            }
        }
        for (int i = 0; i < feats.length; i++) {
            int index = i;
            if (feats[i].get().equals("RANDOM") && availableFeats.get() > i) {
                if (!possibleFeats.isEmpty()) {
                    feats[i].set(possibleFeats.get((int) (Math.random() * possibleFeats.size())).get());
                    if (!getBoolean(new String[]{"feats", feats[i].get(), "repeatable"})) {
                        possibleFeats.removeIf(feat -> feat.get().equals(feats[index].get()));
                    }
                }
            } else if (availableFeats.get() <= i) {
                break;
            }
        }

        for (int i = 0; i < feats.length; i++) {
            if (featOnes[i].get().equals("RANDOM") && availableFeats.get() > i) {
                ObservableString[] featAbilities = featsAbilities[i];
                // Filter out empty strings
                List<String> validAbilities = new ArrayList<>();
                for (ObservableString ability : featAbilities) {
                    if (!ability.get().isEmpty()) {
                        validAbilities.add(ability.get());
                    }
                }
                if (!validAbilities.isEmpty()) {
                    featOnes[i].set(validAbilities.get((int) (Math.random() * validAbilities.size())));
                }
            }

            if (featTwos[i].get().equals("RANDOM") && availableFeats.get() > i) {
                ObservableString[] featAbilities = featsAbilities[i];
                // Filter out empty strings
                List<String> validAbilities = new ArrayList<>();
                for (ObservableString ability : featAbilities) {
                    if (!ability.get().isEmpty()) {
                        validAbilities.add(ability.get());
                    }
                }
                if (!validAbilities.isEmpty()) {
                    featTwos[i].set(validAbilities.get((int) (Math.random() * validAbilities.size())));
                }
            }
        }

        if (name.get().equals("")) {
            String[] availableNames = switch (gender.get()) {
                case "MALE" -> getGroup(new String[]{"species", species.get(), "lineages", lineage.get(), "males"});
                case "FEMALE" -> getGroup(new String[]{"species", species.get(), "lineages", lineage.get(), "females"});
                default -> new String[0];
            };
            String[] availableLasts = getGroup(new String[]{"species", species.get(), "lineages", lineage.get(), "lasts"});

            if (availableNames == null || availableNames.length == 0) {
                availableNames = switch (gender.get()) {
                    case "MALE" -> getGroup(new String[]{"species", species.get(), "males"});
                    case "FEMALE" -> getGroup(new String[]{"species", species.get(), "females"});
                    default -> new String[0];
                };
            }
            if (availableLasts == null || availableLasts.length == 0) {
                availableLasts = getGroup(new String[]{"species", species.get(), "lasts"});
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

        if (classEquipment[0].get().equals("RANDOM")) {
            String[] possibleEquipments = new String[]{classe.get(), "GOLD"};
            classEquipment[0].set(possibleEquipments[(int) (Math.random() * possibleEquipments.length)]);
        }

        if (classEquipment[0].get().equals(classe.get())) {
            String[] equipments = getGroup(new String[]{"classes", classe.get(), "equipment"});
            int index = 1;
            for (String equipment : equipments) {
                if (Arrays.asList(sets).contains(equipment)) {
                    String[] options = getGroup(new String[]{"sets", equipment});
                    classEquipment[index].set(options[(int) (Math.random() * options.length)]);
                    index++;
                }
            }
        }

        if (backgroundEquipment[0].get().equals("RANDOM")) {
            String[] possibleEquipments = new String[]{background.get(), "GOLD"};
            backgroundEquipment[0].set(possibleEquipments[(int) (Math.random() * possibleEquipments.length)]);
        }

        if (backgroundEquipment[0].get().equals(background.get())) {
            String[] equipments = getGroup(new String[]{"backgrounds", background.get(), "equipment"});
            int index = 1;
            for (String equipment : equipments) {
                if (Arrays.asList(sets).contains(equipment)) {
                    String[] options = getGroup(new String[]{"sets", equipment});
                    backgroundEquipment[index].set(options[(int) (Math.random() * options.length)]);
                    index++;
                }
            }
        }

        List<String> usedProficiencies = new ArrayList<>();
        for (ObservableString proficiency : toolProficiencies.asList()) {
            usedProficiencies.add(proficiency.get());
        }
        for (ObservableString proficiency : armorProficiencies.asList()) {
            usedProficiencies.add(proficiency.get());
        }
        for (ObservableString proficiency : weaponProficiencies.asList()) {
            usedProficiencies.add(proficiency.get());
        }
        for (Proficiency proficiency : choiceProficiencies.asList()) {
            if (!proficiency.getName().equals("RANDOM")) {
                usedProficiencies.add(proficiency.getName());
            }
        }
        for (Proficiency choiceProficiency : choiceProficiencies.asList()) {
            if (choiceProficiency.getName().equals("RANDOM")) {
                String[] options = getGroup(new String[]{"sets", choiceProficiency.getGroup()});
                // Remove used proficiencies
                List<String> availableOptions = new ArrayList<>();
                for (String option : options) {
                    if (!usedProficiencies.contains(option)) {
                        availableOptions.add(option);
                    }
                }
                choiceProficiency.setName(availableOptions.get((int) (Math.random() * availableOptions.size())));
                usedProficiencies.add(choiceProficiency.getName());
            }
        }

        if (!availableSpells.isEmpty() && maxSpells.get() > spells.size()) {
            int n = maxSpells.get() - spells.size();
            n = Math.min(n, availableSpells.size());
            List<Spell> possibleSpells = new ArrayList<>(availableSpells.asList());

            for (int i = 0; i < n; i++) {
                int randomIndex = (int) (Math.random() * possibleSpells.size());
                Spell selectedSpell = possibleSpells.remove(randomIndex);
                spells.add(selectedSpell);
            }
        }

        if (!availableCantrips.isEmpty() && maxCantrips.get() > cantrips.size()) {
            int n = maxCantrips.get() - cantrips.size();
            n = Math.min(n, availableCantrips.size());
            List<Spell> possibleCantrips = new ArrayList<>(availableCantrips.asList());

            for (int i = 0; i < n; i++) {
                int randomIndex = (int) (Math.random() * possibleCantrips.size());
                Spell selectedCantrip = possibleCantrips.remove(randomIndex);
                cantrips.add(selectedCantrip);
            }
        }

        if (healthMethod.get().equals("RANDOM")) {
            healthMethod.set("CUSTOM_M");
            health.set(fixedHealth.get() + (level.get() - 1) * (int) (Math.random() * hitDie.get()));
        }

        
        switch (generationMethod.get()) {
            case "STANDARD_ARRAY" -> {
                int[] standardArray = new int[]{15, 14, 13, 12, 10, 8};
                List<Integer> availableScores = new ArrayList<>();
                List<Integer> takenScores = new ArrayList<>();
                for (ObservableString abilityBaseShown : abilityBasesShown) {
                    if (!abilityBaseShown.get().equals("RANDOM")) {
                        int score = Integer.parseInt(abilityBaseShown.get());
                        takenScores.add(score);
                    }
                }
                for (int score : standardArray) {
                    if (!takenScores.contains(score)) {
                        availableScores.add(score);
                    }
                }
                for (int i = 0; i < abilityBasesShown.length; i++) {
                    if (abilityBasesShown[i].get().equals("RANDOM")) {
                        int randomIndex = (int) (Math.random() * availableScores.size());
                        int score = availableScores.remove(randomIndex);
                        abilityBases[i].set(score);
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
}
