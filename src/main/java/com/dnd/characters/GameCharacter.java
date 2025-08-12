package com.dnd.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.TranslationManager;
import com.dnd.utils.BindingBoolean;
import com.dnd.utils.BindingInteger;
import com.dnd.utils.ObservableBoolean;
import com.dnd.utils.ObservableInteger;
import com.dnd.utils.ObservableString;

public class GameCharacter {
    private final String[] skillNames; // Get the names of all skills
    private final String[] abilityNames;
    private final String[] sets;

    private final ObservableString creatureType;
    private final ObservableString languageOne;
    private final ObservableString languageTwo;
    private final ObservableString height;
    private final ObservableString weight;
    private final ObservableString name;
    private final ObservableString gender;
    private final ObservableString subclass;
    private final ObservableString lineage;
    private final ObservableString alignment;
    private final ObservableString generationMethod;
    private final ObservableString healthMethod;
    private final ObservableString levelShown;
    private final ObservableString classe;
    private final ObservableString species;
    private final ObservableString background;
    private final ObservableString size;
    private final ObservableString originFeat;
    private final ObservableString[] feats;
    private final ObservableString[] availableSizes;
    private final ObservableString[] availableSubclasses;
    private final ObservableString[] availableLineages;
    private final ObservableString[] abilityBasesShown;
    
    private final List<ObservableString> actives;
    private final List<ObservableString> passives;
    private final List<ObservableString> weaponProficiencies;
    private final List<ObservableString> armorProficiencies;
    private final List<ObservableString> toolProficiencies;
    private final List<ObservableString> classEquipment;
    private final List<ObservableString> backgroundEquipment;
    private final List<ObservableString> choiceProficiencies;

    // Instead of this I could turn availableSubclasses and availableLineages into Lists, but it shouldn't change much
    private final int maxSubclasses; 
    private final int maxLineages;
    private final int maxFeats;
    private final int[] skillAbilities;     

    private final ObservableInteger generationPointsObservable;
    private final ObservableInteger givenBonusesObservable;
    private final ObservableInteger givenSkillsObservable;
    private final ObservableInteger level;
    private final ObservableInteger[] abilityBases;

    private BindingInteger initiativeBonus;
    private BindingInteger proficiencyBonus;
    private BindingInteger speed;
    private BindingInteger darkvision;
    private BindingInteger armorClass;
    private BindingInteger health;
    private BindingInteger hitDie;
    private BindingInteger givenBonuses;
    private BindingInteger givenSkills;
    private BindingInteger generationPoints;
    private BindingInteger availableFeats;
    private final BindingInteger[] abilities;
    private final BindingInteger[] abilityModifiers;
    private final BindingInteger[] savingThrowBonuses;
    private final BindingInteger[] savingThrowModifiers;
    private final BindingInteger[] skillBonuses;
    private final BindingInteger[] skillModifiers;

    private final ObservableBoolean[] availableSkills;
    private final ObservableBoolean[] abilityPlusOnes;
    private final ObservableBoolean[] abilityPlusTwos;
    private final ObservableBoolean[] fixedSkills;
    
    private final BindingBoolean[] savingThrowProficiencies;
    private final BindingBoolean[] availablePluses;
    private final BindingBoolean[] availableMinuses;
    private final BindingBoolean[] availablePlusOnes;
    private final BindingBoolean[] availablePlusTwos;
    private final BindingBoolean[] skillProficiencies;

    public GameCharacter() {
        skillNames = getGroup(new String[] {"skills"});
        abilityNames = getGroup(new String[] {"abilities"});
        sets = getGroup(new String[] {"sets"});
        int skillCount = skillNames.length;
        int abilityCount = abilityNames.length;

        // Initialize with default values
        skillAbilities = new int[skillCount];
        abilityBases = new ObservableInteger[abilityCount];
        abilities = new BindingInteger[abilityCount];
        abilityModifiers = new BindingInteger[abilityCount];
        savingThrowBonuses = new BindingInteger[abilityCount];
        savingThrowModifiers = new BindingInteger[abilityCount];
        skillBonuses = new BindingInteger[skillCount];
        skillModifiers = new BindingInteger[skillCount];

        abilityPlusOnes = new ObservableBoolean[abilityCount];
        abilityPlusTwos = new ObservableBoolean[abilityCount];
        savingThrowProficiencies = new BindingBoolean[abilityCount];
        skillProficiencies = new BindingBoolean[skillCount];

        abilityBasesShown = new ObservableString[abilityCount];

        availableSkills = new ObservableBoolean[skillCount];
        fixedSkills = new ObservableBoolean[skillCount];

        levelShown = new ObservableString("RANDOM");
        level = new ObservableInteger(0);

        bindLevel();
        bindProficiencyBonus();

        availablePlusOnes = new BindingBoolean[abilityCount];
        availablePlusTwos = new BindingBoolean[abilityCount];
        availablePluses = new BindingBoolean[abilityCount];
        availableMinuses = new BindingBoolean[abilityCount];

        background = new ObservableString("RANDOM");

        // Be careful as givenBonuses has a circular dependency with availablePlusOnes and availablePlusTwos
        givenBonuses = new BindingInteger(() -> 0);
        givenBonusesObservable = new ObservableInteger(0);
        generationPoints = new BindingInteger(() -> 0);
        generationPointsObservable = new ObservableInteger(0);

        for (int i = 0; i < abilityBases.length; i++) {
            // Initialize each ability with a default value of 10
            abilityBases[i] = new ObservableInteger(0);

            abilityBasesShown[i] = new ObservableString("RANDOM");

            abilityPlusOnes[i] = new ObservableBoolean(false);
            abilityPlusTwos[i] = new ObservableBoolean(false);
            
            bindAbilityBase(i);
        }
        
        for (int i = 0; i < abilityBases.length; i++) {
            bindAvailablePlusOne(i);
            bindAvailablePlusTwo(i);

            bindAvailablePluses(i);
            bindAvailableMinuses(i);

        }

        // I had to separate into 2 loops or FinalAbility wouldn't read the +1/2. Why? I still need to figure out.

        for (int i = 0; i < abilityBases.length; i++) {
            bindFinalAbility(i);
            bindAbilityModifier(i);
        }

        bindGivenBonuses();

        bindFixedSkills();

        classe = new ObservableString("RANDOM");
        subclass = new ObservableString("RANDOM");
        maxSubclasses = getInt(new String[] {"max_subclasses"});
        availableSubclasses = new ObservableString[maxSubclasses];
        bindAvailableSubclasses();

        for (int i = 0; i < abilityBases.length; i++) {
            bindSavingThrowProficiencies(i);

            bindSavingThrowBonus(i);
            bindSavingThrowModifier(i);
        }

        givenSkills = new BindingInteger(() -> 0);
        givenSkillsObservable = new ObservableInteger(0);

        for (int i = 0; i < skillBonuses.length; i++) {
            skillAbilities[i] = getInt(new String[] {"skills", skillNames[i], "ability"});

            bindSkillProficiency(i);
            bindSkillBonus(i);
            bindSkillModifier(i);
            bindAvailableSkills(i);
        }

        bindGivenSkills();

        species = new ObservableString("RANDOM");
        lineage = new ObservableString("RANDOM");
        maxLineages = getInt(new String[] {"max_lineages"});
        availableLineages = new ObservableString[maxLineages];
        bindAvailableLineages();

        healthMethod = new ObservableString("MEDIUM_HP");
        generationMethod = new ObservableString("STANDARD_ARRAY");
        bindGenerationPoints();
        bindGenerationMethod();

        name = new ObservableString("");
        gender = new ObservableString("RANDOM");
        alignment = new ObservableString("RANDOM");

        bindSpeed();
        bindDarkvision();
        bindArmorClass();
        bindInitiativeBonus();

        bindHitDie();
        bindHealth();

        actives = new ArrayList<>();
        bindActives();

        passives = new ArrayList<>();
        bindPassives();
        
        height = new ObservableString("");
        weight = new ObservableString("");

        languageOne = new ObservableString("RANDOM");
        languageTwo = new ObservableString("RANDOM");

        creatureType = new ObservableString("");
        bindCreatureType();

        availableSizes = new ObservableString[2];
        size = new ObservableString("");
        bindAvailableSizes();

        maxFeats = getInt(new String[] {"max_feats"});
        feats = new ObservableString[maxFeats];
        for (int i = 0; i < feats.length; i++) {
            feats[i] = new ObservableString("RANDOM");
        }
        bindAvailableFeats();

        originFeat = new ObservableString("");
        bindOriginFeat();

        weaponProficiencies = new ArrayList<>();
        bindWeaponProficiencies();
        armorProficiencies = new ArrayList<>();
        bindArmorProficiencies();
        toolProficiencies = new ArrayList<>();
        bindToolProficiencies();

        classEquipment = new ArrayList<>();
        bindClassEquipment();
        backgroundEquipment = new ArrayList<>();
        bindBackgroundEquipment();
        choiceProficiencies = new ArrayList<>();
        bindChoiceProficiencies();
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

    public ObservableString getFeats(int index) {
        return feats[index];
    }

    public int getMaxLineages() {
        return maxLineages;
    }

    public int getMaxSubclasses() {
        return maxSubclasses;
    }

    public int getMaxFeats() {
        return maxFeats;
    }

    public ObservableInteger getAvailableFeats() {
        return availableFeats;
    }

    public ObservableInteger getAbilityBase(int index) {
        return abilityBases[index];
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

    public List<ObservableString> getActives() {
        return actives;
    }

    public List<ObservableString> getPassives() {
        return passives;
    }

    public List<ObservableString> getWeaponProficiencies() {
        return weaponProficiencies;
    }

    public List<ObservableString> getArmorProficiencies() {
        return armorProficiencies;
    }

    public List<ObservableString> getToolProficiencies() {
        return toolProficiencies;
    }

    public List<ObservableString> getClassEquipment() {
        return classEquipment;
    }

    public List<ObservableString> getBackgroundEquipment() {
        return backgroundEquipment;
    }

    public List<ObservableString> getChoiceProficiencies() {
        return choiceProficiencies;
    }

    // Binders

    private void bindAvailableSubclasses() {
    // Listen for changes to the 'classe' property and update availableSubclasses accordingly
        Runnable updateSubclasses = () -> {
            if (level.get() >= 3) {
                String[] subclasses = getGroup(new String[] {"classes", classe.get(), "subclasses"});
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
                originFeat.set(getString(new String[] {"backgrounds", newVal, "feat"}));
            }
        );
    }

    private void bindAvailableSizes() {
        species.addListener(
            (newVal) -> {
                String[] sizes = getGroup(new String[] {"species", newVal, "size"});
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
        availableFeats = new BindingInteger(
            () -> {
                int[] possibleFeats = getInts(new String[] {"classes", classe.get(), "feats"});
                int i = 0;
                if (possibleFeats != null) {
                    for (int possibleFeat : possibleFeats) {
                        if (possibleFeat <= level.get()) {
                            i++;
                        } else {
                            return i;
                        }
                    }
                }
                return i;
            },
            level,
            classe
        );



        availableFeats.addListener(
            (newVal) -> {
                for (int i = newVal; i < feats.length; i++) {
                    feats[i].set("RANDOM");
                }
            }
        );
    }

    private void bindAvailableLineages() {
        species.addListener(
            (newVal) -> {
                String[] lineages = getGroup(new String[] {"species", newVal, "lineages"});
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
        givenBonuses = new BindingInteger(
            () -> {
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
                return total;
            },
            abilityPlusOnes[0], abilityPlusOnes[1], abilityPlusOnes[2], abilityPlusOnes[3], abilityPlusOnes[4], abilityPlusOnes[5],
            abilityPlusTwos[0], abilityPlusTwos[1], abilityPlusTwos[2], abilityPlusTwos[3], abilityPlusTwos[4], abilityPlusTwos[5]
        );
        givenBonuses.addListener(
            (newVal) -> {
                givenBonusesObservable.set(newVal);
            }
        );
    }

    private void bindGivenSkills() {
        // Bind givenSkills to track the total number of skill proficiencies given
        givenSkills = new BindingInteger(
            () -> {
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
                return total;
            },
            // It only works if you pass all the skillProficiencies and fixedSkills, so I did it manually. Maybe improvable, but should be fine for now.
            skillProficiencies[0], skillProficiencies[1], skillProficiencies[2], skillProficiencies[3], skillProficiencies[4], skillProficiencies[5], skillProficiencies[6], skillProficiencies[7], skillProficiencies[8], skillProficiencies[9], skillProficiencies[10], skillProficiencies[11], skillProficiencies[12], skillProficiencies[13], skillProficiencies[14], skillProficiencies[15], skillProficiencies[16], skillProficiencies[17],
            fixedSkills[0], fixedSkills[1], fixedSkills[2], fixedSkills[3], fixedSkills[4], fixedSkills[5], fixedSkills[6], fixedSkills[7], fixedSkills[8], fixedSkills[9], fixedSkills[10], fixedSkills[11], fixedSkills[12], fixedSkills[13], fixedSkills[14], fixedSkills[15], fixedSkills[16], fixedSkills[17]
        );
        givenSkills.addListener(
            (newVal) -> {
                givenSkillsObservable.set(newVal);
            }
        );
    }

    private void bindCreatureType() {
        species.addListener(
            (newVal) -> {
                creatureType.set(getString(new String[] {"species", newVal, "type"}));
            }
        );
    }

    private void bindAvailablePlusOne(int index) {
        availablePlusOnes[index] = new BindingBoolean(
            () -> {
                String[] possibleAbilities = getGroup(new String[] {"backgrounds", background.get(), "abilities"});
                return Arrays.asList(possibleAbilities).contains(abilityNames[index])
                    && !abilityPlusTwos[index].get()
                    && (givenBonusesObservable.get() < 3 || abilityPlusOnes[index].get());
            },
            background,
            abilityPlusTwos[0],
            givenBonusesObservable
        );
        availablePlusOnes[index].addListener(
            (newVal) -> {
                if (!newVal && abilityPlusOnes[index].get()) {
                    abilityPlusOnes[index].set(false);
                }
            }
        );
    }

    private void bindAvailablePlusTwo(int index) {
        availablePlusTwos[index] = new BindingBoolean(
            () -> {
                String[] possibleAbilities = getGroup(new String[] {"backgrounds", background.get(), "abilities"});
                return Arrays.asList(possibleAbilities).contains(abilityNames[index])
                    && !abilityPlusOnes[index].get()
                    && (givenBonusesObservable.get() < 2 || abilityPlusTwos[index].get());
            },
            background,
            abilityPlusOnes[index],
            givenBonusesObservable
        );
        availablePlusTwos[index].addListener(
            (newVal) -> {
                if (!newVal && abilityPlusTwos[index].get()) {
                    abilityPlusTwos[index].set(false);
                }
            }
        );
    }

    private void bindAvailablePluses(int index) {
        availablePluses[index] = new BindingBoolean(
            () -> {
                int value = abilityBases[index].get();
                int threshold = 1;
                if (value >= 13) {
                    threshold = 2;
                }
                return generationPointsObservable.get() >= threshold
                    && value < 15;
            },
            generationPointsObservable,
            abilityBases[index]
        );
    }

    private void bindAvailableMinuses(int index) {
        availableMinuses[index] = new BindingBoolean(
            () -> {
                int value = abilityBases[index].get();
                int threshold = 1;
                if (value > 13) {
                    threshold = 2;
                }
                return generationPointsObservable.get() + threshold <= 27
                    && value > 8;
            },
            generationPointsObservable,
            abilityBases[index]
        );
    }

    private void bindAvailableSkills(int index) {
        availableSkills[index] = new BindingBoolean(
            () -> {
                String[] possibleSkills = getGroup(new String[] {"classes", classe.get(), "skills"});
                int maxSkills = getInt(new String[] {"classes", classe.get(), "skills_number"});
                return Arrays.asList(possibleSkills).contains(skillNames[index])
                    && !fixedSkills[index].get()
                    && (givenSkillsObservable.get() < maxSkills || skillProficiencies[index].get())
                    && givenSkillsObservable.get() <= maxSkills;
            },
            classe,
            fixedSkills[index],
            givenSkillsObservable
        );

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
                String[] possibleSkills = getGroup(new String[] {"backgrounds", newVal, "skills"});
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
        abilities[index] = new BindingInteger(
            () -> {
                int base = abilityBases[index].get(); // ObservableInteger
                int one = abilityPlusOnes[index].get() ? 1 : 0;
                int two = abilityPlusTwos[index].get() ? 2 : 0;
                return (base == 0 ? 10 : base) + one + two;
            },
            abilityBases[index],
            abilityPlusOnes[index],
            abilityPlusTwos[index]
        );
    }

    private void bindAbilityModifier(int index) {
        // Bind the abilityModifier to the corresponding ability
        abilityModifiers[index] = new BindingInteger(
            () -> 
                (abilities[index].get() - 10) / 2,
            abilities[index]
        );
    }

    private void bindSavingThrowProficiencies(int index) {
        // Bind the savingThrowProficiencies to the corresponding ability
        savingThrowProficiencies[index] = new BindingBoolean(
            () -> {
                String[] possibleSaves = getGroup(new String[] {"classes", classe.get(), "abilities"});
                return Arrays.asList(possibleSaves).contains(abilityNames[index]);
            },
            classe
        );
    }

    private void bindSavingThrowBonus(int index) {
        // Bind savingThrowBonuses[index] to include proficiencyBonus if savingThrowProficiencies[index] is true
        // Uselessely convoluted, but it refused to believe that proficiencyBonus can't be null. I don't like seeing warnings, so I did this.
        savingThrowBonuses[index] = new BindingInteger(
            () -> {
                Boolean prof = savingThrowProficiencies[index].get();
                Integer bonus = proficiencyBonus.get();
                return (prof != null && prof && bonus != null) ? bonus : 0;
            },
            savingThrowProficiencies[index],
            proficiencyBonus
        );
    }

    private void bindGenerationPoints() {
        generationPoints = new BindingInteger(
            () -> {
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
                return points;
            },
            generationMethod,
            abilityBases[0], abilityBases[1], abilityBases[2], abilityBases[3], abilityBases[4], abilityBases[5]
        );
        generationPoints.addListener(newVal -> {
            generationPointsObservable.set(newVal);
        });
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
        savingThrowModifiers[index] = new BindingInteger(
            () -> 
            abilityModifiers[index].get() + savingThrowBonuses[index].get(),
            abilityModifiers[index],
            savingThrowBonuses[index]
        );
    }

    private void bindSkillModifier(int index) {
        // Bind the skillModifier to the corresponding ability
        skillModifiers[index] = new BindingInteger(() -> 
            abilityModifiers[skillAbilities[index]].get() + skillBonuses[index].get(),
            abilityModifiers[skillAbilities[index]],
            skillBonuses[index]
        );
    }

    private void bindSkillBonus(int index) {
        skillBonuses[index] = new BindingInteger(
            () -> {
                Boolean prof = skillProficiencies[index].get();
                Integer bonus = proficiencyBonus.get();
                return (prof != null && prof && bonus != null) ? bonus : 0;
            },
            skillProficiencies[index],
            proficiencyBonus
        );
    }

    private void bindSkillProficiency(int index) {
        // Bind the skillModifier to the corresponding ability
        skillProficiencies[index] = new BindingBoolean(() -> false);

        fixedSkills[index].addListener(
            (newVal) -> {
                if (newVal) {
                    skillProficiencies[index].set(newVal);
                }
            }
        );
    }

    private void bindSpeed() {
        speed = new BindingInteger(
            () -> {
                int baseSpeed  = getInt(new String[] {"species", species.get(), "speed"});
                if (baseSpeed > 0) {
                    return baseSpeed;
                }
                return 30; // Default speed if not defined
            },
            species,
            lineage
        );
    }

    private void bindDarkvision() {
        darkvision = new BindingInteger(
            () -> {
                int baseDarkvision = getInt(new String[] {"species", species.get(), "darkvision"});
                return baseDarkvision; // If darkvision is defined in the lineage, use that, otherwise use the species' value
            },
            species,
            lineage
        );
    }

    private void bindArmorClass() {
        armorClass = new BindingInteger(
            () ->
                abilityModifiers[1].get() + 10,
            abilityModifiers[1]
        );
    }

    private void bindProficiencyBonus() {
        proficiencyBonus = new BindingInteger(
            () -> 
                (level.get() + 7) / 4,
            level
        );
    }

    private void bindInitiativeBonus() {
        initiativeBonus = new BindingInteger(
            () -> 
                abilityModifiers[1].get(),
            abilityModifiers[1]
        );
    }
    
    private void bindHealth(){
        health = new BindingInteger(
            () ->
                (level.get() - 1) * (hitDie.get() / 2 + 1 + abilityModifiers[2].get()) + hitDie.get() + abilityModifiers[2].get(),
            level,
            hitDie,
            abilityModifiers[2]
        );
    }

    private void bindHitDie(){
        hitDie = new BindingInteger(
            () -> {
                int baseHitDie = getInt(new String[] {"classes", classe.get(), "hit_die"});
                return baseHitDie > 0 ? baseHitDie : 4;
            },
            classe
        );
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

    private void bindActives() { // Whenever this gets updated, you need to also update the equivalent in ViewModel
        Runnable updateActives = () -> {
            actives.clear(); // Keep the list in sync
            String[] activeNames = getGroup(new String[] {"species", species.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    if (level.get() >= getInt(new String[] {"species", species.get(), "actives", active})) {
                        actives.add(new ObservableString(active));
                    }
                }
            }

            activeNames = getGroup(new String[] {"species", species.get(), "lineages", lineage.get(), "actives"});
            if (activeNames != null) {
                for (String active : activeNames) {
                    if (level.get() >= getInt(new String[] {"species", species.get(), "lineages", lineage.get(), "actives", active})) {
                        actives.add(new ObservableString(active));
                    }
                }
            }
        };
        
        species.addListener(_ -> updateActives.run());
        lineage.addListener(_ -> updateActives.run());
        level.addListener(_ -> updateActives.run());
        updateActives.run();
    }

    private void bindPassives() { // Whenever this part of code gets updated, you need to also update the equivalent in ViewModel
        Runnable updatePassives = () -> {
            passives.clear(); // Keep the list in sync
            String[] passiveNames = getGroup(new String[] {"species", species.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[] {"species", species.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }

            passiveNames = getGroup(new String[] {"species", species.get(), "lineages", lineage.get(), "passives"});
            if (passiveNames != null) {
                for (String passive : passiveNames) {
                    if (level.get() >= getInt(new String[] {"species", species.get(), "lineages", lineage.get(), "passives", passive})) {
                        passives.add(new ObservableString(passive));
                    }
                }
            }
        };

        species.addListener(_ -> updatePassives.run());
        lineage.addListener(_ -> updatePassives.run());
        level.addListener(_ -> updatePassives.run());
        updatePassives.run();
    }

    private void bindWeaponProficiencies() {
        Runnable updateWeaponProficiencies = () -> {
            weaponProficiencies.clear();
        };

        updateWeaponProficiencies.run();
    }

    private void bindArmorProficiencies() {
        Runnable updateArmorProficiencies = () -> {
            armorProficiencies.clear();
        };

        updateArmorProficiencies.run();
    }

    private void bindToolProficiencies() {
        Runnable updateToolProficiencies = () -> {
            toolProficiencies.clear();
            // in the 2024 rules only one tool proficiency is given for each background. Done like this for future compatibility
            String[] tools = getGroup(new String[] {"backgrounds", background.get(), "tools"});

            for (String tool : tools) {
                if (tool != null) {
                    toolProficiencies.add(new ObservableString(tool));
                }
            }
        };

        background.addListener(_ -> updateToolProficiencies.run());
        updateToolProficiencies.run();
    }

    private void bindClassEquipment() {
        classe.addListener((newVal) -> {
            classEquipment.clear();

            classEquipment.add(new ObservableString(newVal));
            
            String[] equipments = getGroup(new String[] {"classes", newVal, "equipment"});
            for (String equipment : equipments) {
                if (Arrays.asList(sets).contains(equipment)) {
                    classEquipment.add(new ObservableString("RANDOM"));
                }
            }
        });
    }

    private void bindBackgroundEquipment() {
        background.addListener((newVal) -> {
            backgroundEquipment.clear();

            backgroundEquipment.add(new ObservableString(newVal));
            
            String[] equipments = getGroup(new String[] {"backgrounds", newVal, "equipment"});
            for (String equipment : equipments) {
                if (Arrays.asList(sets).contains(equipment)) {
                    backgroundEquipment.add(new ObservableString("RANDOM"));
                }
            }
        });
    }

    private void bindChoiceProficiencies() {
        background.addListener((newVal) -> {
            choiceProficiencies.clear();
            
            String[] equipments = getGroup(new String[] {"backgrounds", newVal, "tools"});
            for (String equipment : equipments) {
                if (Arrays.asList(sets).contains(equipment)) {
                    choiceProficiencies.add(new ObservableString("RANDOM"));
                }
            }
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
}