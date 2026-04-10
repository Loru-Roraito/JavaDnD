package com.dnd.utils.items;

import com.dnd.backend.GameCharacter;
import com.dnd.backend.TraitManager;
import com.dnd.utils.observables.ObservableInteger;
import com.dnd.utils.observables.ObservableString;

public class Trait implements MyItems<Trait> {
    private final GameCharacter character;
    private final ObservableString name;

    private final ObservableInteger charge;
    private final ObservableInteger chargesLeft;
    private final ObservableInteger damage;

    private final ObservableInteger level;
    
    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String value) {
        name.set(value);
    }

    @Override
    public Trait copy() {
        Trait newTrait = new Trait(character, name.get(), level);
        newTrait.getChargesLeft().set(chargesLeft.get());
        return newTrait;
    }

    @Override
    public ObservableString getNameProperty() {
        return name;
    }

    public Boolean equals(Trait other) {
        return this.name.get().equals(other.name.get()) && this.level.get().equals(other.level.get());
    }

    public Trait(GameCharacter character, String name) {
        this(character, name, new ObservableInteger(0));
    }

    public Trait(GameCharacter character, String nominative, ObservableInteger level) {
        this.character = character;
        name = new ObservableString(nominative);
        this.level = level;
        
        int[] charges = getTraitInts(new String[] {nominative, "charges"});
        String chargeAbility = getTraitString(new String[] {nominative, "chargeAbility"});
        if (charges.length != 0) {
            charge = new ObservableInteger(charges[level.get()]);
            chargesLeft = new ObservableInteger(charge.get());
            level.addListener((newVal) -> {
                charge.set(charges[newVal]);
                if (chargesLeft.get() > charge.get()) {
                    chargesLeft.set(charge.get());
                }
            });
        } else if (!chargeAbility.equals("")) {
            charge = character.getAbilityModifier(GameCharacter.getAbilityIndex(chargeAbility));
            chargesLeft = new ObservableInteger(charge.get());
            charge.addListener((newVal) -> {
                if (chargesLeft.get() > newVal) {
                    chargesLeft.set(newVal);
                }
            });
        }else {
            charge = new ObservableInteger(-1);
            chargesLeft = new ObservableInteger(-1);
        }

        int[] damages = getTraitInts(new String[] {nominative, "damages"});
        if (damages.length != 0) {
            damage = new ObservableInteger(damages[level.get()]);
            level.addListener((newVal) -> damage.set(damages[newVal]));
        } else {
            damage = new ObservableInteger(0);
        }

        int shortRest = getTraitInt(new String[] {nominative, "short_rest"});
        int longRest = getTraitInt(new String[] {nominative, "long_rest"});

        character.isShortResting().addListener(isResting -> {
            if (isResting && shortRest != 0 && chargesLeft.get() < charge.get()) {
                if (shortRest == -1) {
                    chargesLeft.set(charge.get());
                } else {
                    chargesLeft.set(Math.min(chargesLeft.get() + shortRest, charge.get()));
                }
            }
        });
        character.isLongResting().addListener(isResting -> {
            if (isResting && longRest != 0 && chargesLeft.get() < charge.get()) {
                if (longRest == -1) {
                    chargesLeft.set(charge.get());
                } else {
                    chargesLeft.set(Math.min(chargesLeft.get() + longRest, charge.get()));
                }
            }
        });
    }

    public ObservableInteger getChargesLeft() {
        return chargesLeft;
    }

    public ObservableInteger getCharge() {
        return charge;
    }

    public ObservableInteger getDamage() {
        return damage;
    }

    public String getNominative() {
        return name.get();
    }

    public ObservableInteger getLevel() {
        return level;
    }

    private String getTraitString(String[] key) {
        return TraitManager.getInstance().getString(key);
    }

    private int getTraitInt(String[] key) {
        return TraitManager.getInstance().getInt(key);
    }

    private int[] getTraitInts(String[] key) {
        return TraitManager.getInstance().getInts(key);
    }
}
