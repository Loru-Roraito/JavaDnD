package com.dnd.ui.panes;

import com.dnd.ViewModel;

import java.util.Arrays;

import com.dnd.TranslationManager;
import com.dnd.ui.tooltip.TooltipButton;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.items.Item;
import com.dnd.ui.tabs.InfoTab;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TabPane;
import javafx.collections.FXCollections;

public class CombatPane extends GridPane {
    private final ViewModel character;
    private final InfoTab infoTab;
    private final CheckBox critical;
    public CombatPane(TabPane mainTabPane, ViewModel character, InfoTab infoTab) {
        this.character = character;
        this.infoTab = infoTab;
        getStyleClass().add("grid-pane");
        
        TooltipButton attack = new TooltipButton(getTranslation("ATTACK_ROLL"), mainTabPane);
        add(attack, 0, 1);
        TooltipButton damage = new TooltipButton(getTranslation("DAMAGE"), mainTabPane);
        add(damage, 1, 1);
        TooltipButton bonusAttack = new TooltipButton(getTranslation("BONUS_ATTACK"), mainTabPane);
        add(bonusAttack, 0, 2);
        critical = new CheckBox(getTranslation("CRITICAL_HIT"));
        add(critical, 0, 0);
        TooltipButton bonusDamage = new TooltipButton(getTranslation("DAMAGE"), mainTabPane);
        add(bonusDamage, 1, 2);
        TooltipButton versatileDamage = new TooltipButton(getTranslation("DAMAGE"), mainTabPane);
        add(versatileDamage, 1, 2);
        TooltipLabel finesseLabel = new TooltipLabel(getTranslation("FINESSE_ABILITY"), mainTabPane);
        ObservableList<String> finesses = FXCollections.observableArrayList(
            getTranslation("BEST_ONE"),
            getTranslation("STRENGTH"),
            getTranslation("DEXTERITY")
        );
        TooltipComboBox finesse = new TooltipComboBox(finesses, mainTabPane);
        finesse.valueProperty().bindBidirectional(character.getFinesseAbility());
        add(finesseLabel, 0, 3, 2, 1);
        add(finesse, 0, 4, 2, 1);

        Runnable updateBonusDamage = () -> {
            Item offHand = character.getOffHand().get();
            if (Arrays.asList(offHand.getProperties()).contains("VERSATILE")) {
                bonusAttack.setVisible(true);
                bonusAttack.setText(getTranslation("TWO_HANDED_ATTACK"));
                bonusDamage.setVisible(false);
                versatileDamage.setVisible(true);
            } else if (!offHand.getNominative().equals("NONE_F") && !Arrays.asList(offHand.getProperties()).contains("TWO_HANDED")) {
                bonusAttack.setVisible(true);
                bonusAttack.setText(getTranslation("BONUS_ATTACK"));
                bonusDamage.setVisible(true);
                versatileDamage.setVisible(false);
            } else {
                bonusAttack.setVisible(false);
                bonusDamage.setVisible(false);
                versatileDamage.setVisible(false);
            }
        };
        updateBonusDamage.run();
        character.getOffHand().addListener(_ -> updateBonusDamage.run());

        attack.setOnAction(_ -> {
            Item weapon = character.getMainHand().get();
            attackRoll(weapon);
        });

        bonusAttack.setOnAction(_ -> {
            Item weapon = character.getOffHand().get();
            attackRoll(weapon);
        });

        damage.setOnAction(_ -> {
            Item weapon = character.getMainHand().get();
            damageRoll(weapon, false, false);
        });

        bonusDamage.setOnAction(_ -> {
            Item weapon = character.getOffHand().get();
            damageRoll(weapon, true, false);
        });

        versatileDamage.setOnAction(_ -> {
            Item weapon = character.getOffHand().get();
            damageRoll(weapon, false, true);
        });
    }

    private void attackRoll(Item weapon) {
        if (Arrays.asList(weapon.getProperties()).contains("AMMUNITION")) {
            String ammo = weapon.getAmmo();
            boolean hasAmmo = false;
            for (Item item : character.getItems()) {
                if (item.getNominative().equals(ammo)) {
                    character.getItems().remove(item);
                    hasAmmo = true;
                    break;
                }
            }
            if (!hasAmmo) {
                return;
            }
        }

        int ability = dexOrStr(weapon);
        int modifier = character.getAbilityModifier(ability).get();

        boolean hasMainProficiency = character.hasMainProficiency().get();
        if (hasMainProficiency) {
            modifier += character.getProficiencyBonus().get();
        }

        int result = infoTab.throwDie(1, 20, modifier, false, Arrays.asList(weapon.getProperties()).contains("HEAVY") && character.getSize().get().equals(getTranslation("SMALL")), ability);
        if (result - modifier == 20) {
            // Critical hit
            critical.setSelected(true);
        } else {
            critical.setSelected(false);
        }
    }

    private void damageRoll(Item weapon, boolean isBonus, boolean isVersatile) {
        int modifier = 0;
        int ability = dexOrStr(weapon);
        if (!isBonus) {
            modifier = character.getAbilityModifier(ability).get();
        }

        int hits;
        int damage;
        if (isVersatile) {
            hits = weapon.getVersatileHits();
            damage = weapon.getVersatileDamage();
        } else {
            hits = weapon.getHits();
            damage = weapon.getDamage();

        }

        if (critical.isSelected() && !weapon.getNominative().equals("UNARMED_STRIKE")) {
            hits *= 2;
            critical.setSelected(false);
        }

        infoTab.throwDie(hits, damage, modifier, false, false, ability);
    }

    private int dexOrStr(Item weapon) {
        int ability = 0;
        if (Arrays.asList(weapon.getProperties()).contains("FINESSE")){
            if (character.getFinesseAbility().get().equals(getTranslation("BEST_ONE"))) {
                if (character.getAbility(1).get() > character.getAbility(0).get()) {
                    ability = 1;
                }
            } else if (character.getFinesseAbility().get().equals(getTranslation("DEXTERITY"))) {
                ability = 1;
            }
        } else if (Arrays.asList(weapon.getTags()).contains("RANGED")) {
            ability = 1; // DEX
        }
        return ability;
        
        /* 
        I believe this version is less readable
        if (
            (Arrays.asList(weapon.getProperties()).contains("FINESSE") // FINESSE weapon
                && (character.getFinesseAbility().get().equals(getTranslation("BEST_ONE")) // BEST_ONE mode
                    && character.getAbility(1).get() > character.getAbility(0).get()) // DEX > STR
                || character.getFinesseAbility().get().equals(getTranslation("DEXTERITY"))) // DEX mode
            || Arrays.asList(weapon.getTags()).contains("RANGED")) { // RANGED weapon
                ability = 1; // DEX
        } */
    }

    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}