import sys
from typing import Union, List, Dict
from PyQt6.QtCore import Qt, QPoint, QModelIndex, QLocale, QRegularExpression, QTimer
from PyQt6.QtGui import QPixmap, QHelpEvent, QWheelEvent, QIntValidator, QDoubleValidator, QValidator, QRegularExpressionValidator, QCloseEvent, QKeyEvent, QCursor, QFontMetrics
from PyQt6.QtWidgets import QLineEdit, QApplication, QComboBox, QDialog, QVBoxLayout, QGridLayout, QGroupBox, QLabel, QPushButton, QCheckBox, QScrollArea, QToolTip, QStyledItemDelegate, QMessageBox, QTabWidget, QWidget, QFileDialog, QSizePolicy, QCompleter
import random
import numpy as np
import os
import ast
import copy
import string
import textwrap
import re

BASE_PATH = "C:\\Users\\david\\OneDrive\\Documenti\\DnD\\savedata"

# For now I only have one language and setting, but changing these constants will allow me to easily add more
# Constants for the language
GENERATOR_WINDOW = "Generatore di Personaggi"
SHEET_WINDOW = "Scheda del Personaggio"
NONE_M = "Nessuno" # Need to differentiate between male and female for Italian
NONE_F = "Nessuna"
RANDOM = "Casuale"
RANDOM_STANDARD = "Casuale (Standard)"
RANDOM_RARE = "Casuale (Rara)"
BLOCK_EQUIPMENT_TRANSLATE = "Blocca Equipaggiamento"
ABILITIES_TRANSLATE = "Caratteristiche"
SKILLS_TRANSLATE = "Abilità"
THROW_TRANSLATE = "Tira"
SAVES_TRANSLATE = "Tiri Salvezza"
HEALTH_TRANSLATE = "Salute"
HIT_POINTS_TRANSLATE = "Punti Ferita"
MEDIUM_HP_TRANSLATE = "PF medi"
ARMOR_CLASS_TRANSLATE = "Classe Armatura"
PROFICIENCIES_BONUS_TRANSLATE = "Bonus di Competenza"
INITIATIVE_BONUS_TRANSLATE = "Bonus di Iniziativa"
PASSIVE_PERCEPTION_TRANSLATE = "Percezione Passiva"
SIZE_TRANSLATE = "Taglia"
PARAMETERS_TRANSLATE = "Parametri"
SPECIES_TRANSLATE = "Specie"
LINEAGE_TRANSLATE = "Lignaggio"
BACKGROUND_TRANSLATE = "Background"
CLASS_TRANSLATE = "Classe"
SUBCLASS_TRANSLATE = "Sottoclasse"
LEVEL_TRANSLATE = "Livello"
SPEED_TRANSLATE = "Velocità"
DARKVISION_TRANSLATE = "Scurovisione"
ALIGNMENT_TRANSLATE = "Allineamento"
LOOKS_TRANSLATE = "Aspetto"
NAME_TRANSLATE = "Nome"
GENDER_TRANSLATE = "Genere"
AGE_TRANSLATE = "Età"
HEIGHT_TRANSLATE = "Altezza"
WEIGHT_TRANSLATE = "Peso"
PROFICIENCIES_TRANSLATE = "Competenze"
LANGUAGES_TRANSLATE = "Lingue"
WEAPONS_ARMORS_TOOLS_TRANSLATE = "Armi, Armature e Strumenti"
RESISTANCES_IMMUNITIES_TRANSLATE = "Resistenze e immunità"
MISC_TRANSLATE = "Varie"
EQUIPMENT_TRANSLATE = "Equipaggiamento"
SYSTEM_TRANSLATE = "Sistema"
EDIT_TRANSLATE = "Modifica"
FINISH_TRANSLATE = "Termina"
CONFIRM_TRANSLATE = "Conferma"
AGE_MIN_TRANSLATE = "Età Minima"
AGE_MAX_TRANSLATE = "Età Massima"
SHEET_NUMBER_TRANSLATE = "Numero schede"
SAVE_TRANSLATE = "Salva"
SAVE_AS_TRANSLATE = "Salva come..."
SIMPLE_WEAPONS_TRANSLATE = "Armi semplici"
MARTIAL_WEAPONS_TRANSLATE = "Armi da guerra"
RESISTENT_TRANSLATE = "Resistente a:"
IMMUNE_TRANSLATE = "Immune a:"
ADVANTAGE_AGAINST_TRANSLATE = "Vantaggio contro:"
SKILL_ADVANTAGE_TRANSLATE = "Vantaggio ai tiri su:"
SKILL_DISADVANTAGE_TRANSLATE = "Svantaggio ai tiri su:"
SAVE_ADVANTAGE_TRANSLATE = "Vantaggio ai tiri salvezza su:"
SAVE_DISADVANTAGE_TRANSLATE = "Svantaggio ai tiri salvezza su:"
LEVEL_UP_TRANSLATE = "Livello successivo"
WARNING_UNSAVED_TRANSLATE = "Personaggio non salvato"
YES_TRANSLATE = "Sì"
NO_TRANSLATE = "No"
CANCEL_TRANSLATE = "Annulla"
CLOSE_MESSAGE = "Salvare le modifiche?"
INFO_TRANSLATE = "Info"
MAGIC_TRANSLATE = "Magia"
EXTRA_TRANSLATE = "Extra"
STATUS_TRANSLATE = "Stato"
TEXT_FILE_TRANSLATE = "File di testo"
LOAD_TRANSLATE = "Carica"
SAVE_TEXT = "Salva personaggio"
LOAD_TEXT = "Carica personaggio"
CUSTOMIZATION_TRANSLATE = "Personalizzazione"
STAT_GENERATION_TRANSLATE = "Metodo di generazione"
POINTS_TRANSLATE = "Punti"
FORCED_ADVANTAGE_TRANSLATE = "Vantaggio forzato"
DISABLED_M = "Disabilitato"
ADVANTAGE_TRANSLATE = "Vantaggio"
DISADVANTAGE_TRANSLATE = "Svantaggio"
ABILITY_SCORE_INCREASE = "Caratteristiche"
FEAT_TRANSLATE = "Talento"
ORIGIN_FEAT = "Talento Origine"
GENERAL_FEAT = "Talento Generale"
EQUIPMENT_NEW_TRANSLATE = "Nuovo equipaggiamento"
DAMAGES_TRANSLATE = "danni"
COST_TRANSLATE = "Costo"
RANGE_TRANSLATE = "Gittata"
DAMAGE_VERSATILE_TRANSLATE = "Danno a due mani"
AVAILABLE_LANGUAGES = "Lingue disponibili"
ALL_TRANSLATE_F = "Tutte"
STANDARD_TRANSLATE = "Standard"
CONFIRM_FEATS = "Conferma talenti"
FINESSE_ABILITY_TRANSLATE = "Caratteristica per armi accurate:"
HIGHEST = "Maggiore"
SHORT_REST_TRANSLATE = "Riposo Breve"
LONG_REST_TRANSLATE = "Riposo Lungo"
STOP_REST_TRANSLATE = "Termina Riposo"
HIT_DICE_TRANSLATE = "Dadi Vita"
FEATURES_TRANSLATE = "Abilità attive"
OR_TRANSLATE = "o"
FLY_SPEED_TRANSLATE = "Velocità di Volo"
SAVE_THROW_TRANSLATE = "Tiro Salvezza"
DC_TRANSLATE = "CD"
DIE_TRANSLATE = "Dado"
RESULT_TRANSLATE = "Risultato"
RESULTS_TRANSLATE = "Risultati"
CHANGE_ON_TRANSLATE = "modificabile ogni"

# Conditions
CONDITIONS_TRANSLATE = "Condizioni"
BLINDED = "Accecato"
CHARMED = "Affascinato"
DEAFENED = "Assordato"
EXHAUSTION = "Affaticamento"
FRIGHTENED = "Spaventato"
GRAPPLED = "Afferrato"
INCAPACITATED = "Incapacitato"
INVISIBLE = "Invisibile"
PARALYZED = "Paralizzato"
PETRIFIED = "Pietrificato"
POISONED = "Avvelenato"
PRONE = "Prono"
RESTRAINED = "Trattenuto"
STUNNED = "Stordito"
UNCONSCIOUS = "Incosciente"

# Species
HUMAN = "Umano"
ELF = "Elfo"
AASIMAR = "Aasimar"
DRAGONBORN = "Dragonide"
DWARF = "Nano"
GOLIATH = "Goliath"
HALFLING = "Halfling"
GNOME = "Gnomo"
TIEFLING = "Tiefling"
ORC = "Orco"

# Classes
BARBARIAN = "Barbaro"
BARD = "Bardo"
CLERIC = "Chierico"
DRUID = "Druido"
FIGHTER = "Guerriero"
ROGUE = "Ladro"
WIZARD = "Mago"
MONK = "Monaco"
PALADIN = "Paladino"
RANGER = "Ranger"
SORCERER = "Stregone"
WARLOCK = "Warlock"

# Origins
ACOLYTE = "Accolito"
ARTISAN = "Artigiano"
CHARLATAN = "Ciarlatano"
CRIMINAL = "Criminale"
ENTERTAINER = "Intrattenitore"
FARMER = "Contadino"
GUARD = "Guardia"
GUIDE = "Guida"
HERMIT = "Eremita"
MERCHANT = "Mercante"
NOBLE = "Nobile"
SAGE = "Saggio"
SAILOR = "Marinaio"
SCRIBE = "Scriba"
SOLDIER = "Soldato"
WAYFARER = "Viandante"

ACOLYTE_EQUIPMENT = "Equipaggiamento da Accolito"
ARTISAN_EQUIPMENT = "Equipaggiamento da Artigiano"
CHARLATAN_EQUIPMENT = "Equipaggiamento da Ciarlatano"
CRIMINAL_EQUIPMENT = "Equipaggiamento da Criminale"
ENTERTAINER_EQUIPMENT = "Equipaggiamento da Intrattenitore"
FARMER_EQUIPMENT = "Equipaggiamento da Contadino"
GUARD_EQUIPMENT = "Equipaggiamento da Guardia"
GUIDE_EQUIPMENT = "Equipaggiamento da Guida"
HERMIT_EQUIPMENT = "Equipaggiamento da Eremita"
MERCHANT_EQUIPMENT = "Equipaggiamento da Mercante"
NOBLE_EQUIPMENT = "Equipaggiamento da Nobile"
SAGE_EQUIPMENT = "Equipaggiamento da Saggio"
SAILOR_EQUIPMENT = "Equipaggiamento da Marinaio"
SCRIBE_EQUIPMENT = "Equipaggiamento da Scriba"
SOLDIER_EQUIPMENT = "Equipaggiamento da Soldato"
WAYFARER_EQUIPMENT = "Equipaggiamento da Viandante"

BARBARIAN_EQUIPMENT = "Equipaggiamento da Barbaro"
BARD_EQUIPMENT = "Equipaggiamento da Bardo"
CLERIC_EQUIPMENT = "Equipaggiamento da Chierico"
DRUID_EQUIPMENT = "Equipaggiamento da Druido"
FIGHTER_EQUIPMENT = "Equipaggiamento da Guerriero"
ROGUE_EQUIPMENT = "Equipaggiamento da Ladro"
WIZARD_EQUIPMENT = "Equipaggiamento da Mago"
MONK_EQUIPMENT = "Equipaggiamento da Monaco"
PALADIN_EQUIPMENT = "Equipaggiamento da Paladino"
RANGER_EQUIPMENT = "Equipaggiamento da Ranger"
SORCERER_EQUIPMENT = "Equipaggiamento da Stregone"
WARLOCK_EQUIPMENT = "Equipaggiamento da Warlock"

# Feats
ALERT = "Allerta"
CRAFTER = "Costruttore"
HEALER = "Curatore"
LUCKY = "Fortunato"
MAGIC_INITIATE = "Iniziato alla Magia"
MUSICIAN = "Musicista"
SAVAGE_ATTACKER = "Attaccante Selvaggio"
SKILLED = "Abile"
TAVERN_BRAWLER = "Lottatore da Osteria"
TOUGH = "Resistente"

# Lineages
CALISHITA = "Calishita"
CHONDATHAN = "Chondathan"
DAMARAN = "Damaran"
ILLUSKAN = "Illuskan"
MULAN = "Mulan"
RASHEMI = "Rashemi"
SHOU = "Shou"
TETHYRIAN = "Tethyrian"
TURAMI = "Turami"
WOOD_ELF = "Elfo dei Boschi"
HIGH_ELF = "Elfo Superiore"
DROW = "Drow"
SILVER_DRAGONBORN = "Argento"
WHITE_DRAGONBORN = "Bianco"
BLUE_DRAGONBORN = "Blu"
BRONZE_DRAGONBORN = "Bronzo"
BLACK_DRAGONBORN = "Nero"
GOLD_DRAGONBORN = "Oro"
BRASS_DRAGONBORN = "Ottone"
COPPER_DRAGONBORN = "Rame"
RED_DRAGONBORN = "Rosso"
GREEN_DRAGONBORN = "Verde"
MOUNTAIN_DWARF = "Nano delle Montagne"
HILL_DWARF = "Nano delle Colline"
LIGHTFOOT_HALFLING = "Halfling Piedelesto"
STOUT_HALFLING = "Halfling Tozzo"
FOREST_GNOME = "Gnomo delle Foreste"
ROCK_GNOME = "Gnomo delle Rocce"
CLOUD_GIANT = "Gigante delle Nuvole"
FIRE_GIANT = "Gigante del Fuoco"
FROST_GIANT = "Gigante del Gelo"
HILL_GIANT = "Gigante delle Colline"
STONE_GIANT = "Gigante della Pietra"
STORM_GIANT = "Gigante della Tempesta"
ABYSSAL_TIEFLING = "Tiefling Abissale"
CHTONIC_TIEFLING = "Tiefling Ctonio"
INFERNAL_TIEFLING = "Tiefling Infernale"

# Languages
COMMON_LANGUAGE = "Comune"
COMMON_SIGN_LANGUAGE = "Linguaggio dei Segni"
ELVISH_LANGUAGE = "Elfico"
DRACONIC_LANGUAGE = "Draconico"
DWARVISH_LANGUAGE = "Nanico"
HALFLING_LANGUAGE = "Halfling"
GNOMISH_LANGUAGE = "Gnomico"
ORC_LANGUAGE = "Orchesco"
GOBLIN_LANGUAGE = "Goblin"
GIANT_LANGUAGE = "Gigante"

CELESTIAL_LANGUAGE = "Celestiale"
ABYSSAL_LANGUAGE = "Abissale"
INFERNAL_LANGUAGE = "Infernale"
DEEP_SPEECH = "Linguaggio Profondo"
PRIMORDIAL_LANGUAGE = "Primordiale"
SYLVAN_LANGUAGE = "Silvano"
UNDERCOMMON_LANGUAGE = "Sottocomune"
DRUIDIC_LANGUAGE = "Druidico"
THIEVES_CANT = "Gergo dei Ladri"

ALIGNMENTS = [RANDOM, "Legale Buono", "Legale Neutrale", "Legale Malvagio", "Neutrale Buono", "Neutrale Puro", "Neutrale Malvagio", "Caotico Buono", "Caotico Neutrale", "Caotico Malvagio"]
ALIGNMENTS_1 = ["Caotico", "Neutrale", "Legale"]
ALIGNMENTS_2 = ["Malvagio", "Neutrale", "Buono"]
GENDERS = [RANDOM, "Maschio", "Femmina"]
STAT_GENERATION = ["Standard array", "Point buy", "Casuale", "Personalizzato"]

UNARMED_STRIKE = "Attacco senza Armi"
IMPROVISED_WEAPON = "Arma Improvvisata"

CLUB = "Randello"
DAGGER = "Pugnale"
GREATCLUB = "Randello Pesante"
HANDAXE = "Ascia"
JAVELIN = "Giavellotto"
LIGHT_HAMMER = "Martello Leggero"
MACE = "Mazza"
QUARTERSTAFF = "Bastone Ferrato"
SICKLE = "Falcetto"
SPEAR = "Lancia"
CROSSBOW_LIGHT = "Balestra Leggera"
DART = "Freccetta"
SHORTBOW = "Arco Corto"
SLING = "Fionda"
BATTLEAXE = "Ascia da Battaglia"
FLAIL = "Mazzafrusto"
GLAIVE = "Alabarda"
GREATAXE = "Ascia Bipenne"
GREATSWORD = "Spadone"
HALBERD = "Falcione"
LANCE = "Lancia da Cavaliere"
LONGSWORD = "Spada Lunga"
MAUL = "Maglio"
MORNINGSTAR = "Morning Star"
PIKE = "Picca"
RAPIER = "Stocco"
SCIMITAR = "Scimitarra"
SHORTSWORD = "Spada Corta"
TRIDENT = "Tridente"
WAR_PICK = "Piccone da Guerra"
WARHAMMER = "Martello da Guerra"
WHIP = "Frusta"
BLOWGUN = "Cerbottana"
CROSSBOW_HAND = "Balestra a Mano"
CROSSBOW_HEAVY = "Balestra Pesante"
LONGBOW = "Arco Lungo"
MUSKET = "Moschetto"
PISTOL = "Pistola"

PADDED_ARMOR = "Armatura Imbottita"
LEATHER_ARMOR = "Armatura di Cuoio"
STUDDED_LEATHER_ARMOR = "Armatura di Cuoio Borchiato"
HIDE_ARMOR = "Armatura di Pelle"
CHAIN_SHIRT = "Maglia"
SCALE_MAIL = "Maglia di Scaglie"
BREASTPLATE = "Corazza"
HALF_PLATE = "Mezza Armatura"
RING_MAIL = "Maglia a Scaglie"
CHAIN_MAIL = "Maglia di Maglia"
SPLINT_ARMOR = "Armatura di Piastre"
PLATE_ARMOR = "Armatura Completa"
SHIELD = "Scudo"

BATTLEAXES = "Asce da Battaglia"
HANDAXES = "Asce"
LIGHT_HAMMERS = "Martelli Leggeri"
WARHAMMERS = "Martelli da Guerra"
SHORTSWORDS = "Spade Corte"
LONGSWORDS = "Spade Lunghe"
SHORTBOWS = "Archi Corti"
LONGBOWS = "Archi Lunghi"
RAPIERS = "Stocchi"
CROSSBOW_HANDS = "Balestre a Mano"
LIGHT_ARMORS_TRANSLATE = "Armature Leggere"
MEDIUM_ARMORS_TRANSLATE = "Armature Medie"
HEAVY_ARMORS_TRANSLATE = "Armature Pesanti"
SHIELDS_TRANSLATE = "Scudi"

EXPLORERS_PACK = "Equipaggiamento da Esploratore"
CALLIGRAPHERS_SUPPLIES = "Scorte da Calligrafo"
SMITHS_TOOLS = "Attrezzi da Fabbro"
BREWERS_SUPPLIES = "Scorte da Mescitore"
MASONRY_TOOLS = "Attrezzi da Costruttore"
FORGERY_KIT = "Strumenti da Falsificazione"
HEALERS_KIT = "Strumenti da Curatore"
THIEVES_TOOLS = "Attrezzi da Ladro"
CARTOGRAPHERS_TOOLS = "Attrezzi da Cartografo"
CARPENTERS_TOOLS = "Attrezzi da Carpentiere"
NAVIGATORS_TOOLS = "Attrezzi da Navigatore"
LEATHERWORKERS_TOOLS = "Attrezzi da Conciatore"
MASONS_TOOLS = "Attrezzi da Muratore"
POTTERS_TOOLS = "Attrezzi da Ceramista"
TINKERS_TOOLS = "Attrezzi da Armeggiatore"
WEAVERS_TOOLS = "Attrezzi da Tessitore"
WOODCARVERS_TOOLS = "Attrezzi da Intagliatore"

BOOK_PRAYERS = "Libro di Preghiere"
HOLY_SYMBOL = "Icona Sacra"
PARCHMENT_SHEETS = "Fogli di Pergamena"
ROBE = "Tunica"
POUCH = "Borsa"
TRAVELERS_CLOTHES = "Abiti da viaggiatore"
ROPE = "Fune"
BOOK_HISTORY = "Libro di Storia"
HERBALISM_KIT = "Equipaggiamento da Erborista"
BOOK_PHILOSPHY = "Libro di Filosofia"
LAMP = "Lampada"
OIL_FLASK = "Fiaschetta d'Olio"
ARROW = "Freccia"
NEEDLE = "Ago"
BOLT = "Dardo"
BULLET = "Proiettile"
NET = "Rete"
BEDROLL = "Sacco a Pelo"
QUIVER = "Faretra"
TENT = "Tenda"
MANACLES = "Manette"
HOODED_LANTERN = "Lanterna con Copertura"
IRON_POT = "Pentola di Ferro"
SHOVEL = "Pala"
COSTUME = "Travestimento"
FINE_CLOTHES = "Vestiti Raffinati"
CROWBAR = "Piede di Porco"
MIRROR = "Specchio"
PERFUME = "Profumo"
LADDER = "Scala"
TORCH = "Torcia"
CASE = "Astuccio"
BLOCK_AND_TACKLE = "Paranco"
JUG = "Brocca"
BALL_BEARINGS = "Biglie"
BUCKET = "Secchio"
CALTROPS = "Triboli"
GRAPPLING_HOOK = "Rampino"
BELL = "Campanella"
TINDER_BOX = "Scatola di Fiammiferi"
BASKET = "Cesto"

FIREFLY = "Libellula"
PHOSPHORESCENT_MOSS = "Muschio Fosforescente"

DICE = "Dadi"
DRAGONCHESS = "Scacchi Draconici"
PLAYING_CARDS = "Carte da Gioco"
THREE_DRAGON_ANTE = "Ante Tre Draghi"

BAGPIPES = "Cornamusa"
DRUM = "Tamburo"
DULCIMER = "Dulcimer"
FLUTE = "Flauto"
HORN = "Corno"
LUTE = "Liuto"
LYRE = "Lira"
PAN_FLUTE = "Flauto di Pan"
SHAWN = "Corno Inglese"
VIOL = "Viola"

QUICK_CRAFTING = "Costruzione Veloce"
BATTLE_MEDIC= "Medico da Battaglia"
LUCK_POINTS = "Punti Fortuna"
HEALING_HANDS = "Mani Guaritrici"
CELESTIAL_REVELATION = "Rivelazione Celestiale"
HEAVENLY_WINGS = "Ali Celestiali"
INNER_RADIANCE = "Splendore Interno"
NECROTIC_SHROUD = "Manto Necrotico"
RAGE = "Ira"

LIGHT = "Leggera"
FINESSE = "Accurata"
THROWN = "Lancio"
TWO_HANDED = "A due mani"
VERSATILE = "Versatile"
AMMUNITION = "Munizioni"
LOADING = "Ricarica"
HEAVY = "Pesante"
REACH = "Portata"

WEAPON_MASTERY_TRANSLATE = "Maestria con le Armi"
CLEAVE = "Fendi"
GRAZE = "Sfiora"
NICK = "Intacca"
PUSH = "Spingi"
SAP = "Stordisci"
SLOW = "Rallenta"
TOPPLE = "Abbatti"
VEX = "Infastidisci"

BLUDGEONING = "Contundenti"
PIERCING = "Perforanti"
SLASHING = "Taglienti"
ALL_DAMAGE = "Qualsiasi danno"

MELEE = "Da mischia"
RANGED = "Da distanza"
SIMPLE = "Semplice"
MARTIAL = "Da guerra"

GOLD = "mo"
SILVER = "ma"
COPPER = "mr"
ELECTRUM = "me"
PLATINUM = "mp"

STRENGTH = "Forza"
DEXTERITY = "Destrezza"
CONSTITUTION = "Costituzione"
INTELLIGENCE = "Intelligenza"
WISDOM = "Saggezza"
CHARISMA = "Carisma"
ACROBATICS = "Acrobazia"
ANIMAL_HANDLING = "Addestrare Animali"
ARCANA = "Arcano"
ATHLETICS = "Atletica"
DECEPTION = "Inganno"
HISTORY = "Storia"
INSIGHT = "Intuizione"
INTIMIDATION = "Intimidire"
INVESTIGATION = "Indagare"
MEDICINE = "Medicina"
NATURE = "Natura"
PERCEPTION = "Percezione"
PERFORMANCE = "Intrattenere"
PERSUASION = "Persuasione"
RELIGION = "Religione"
SLEIGHT_OF_HAND = "Rapidità di Mano"
STEALTH = "Furtività"
SURVIVAL = "Sopravvivenza"
MEDIUM = "Media"
SMALL = "Piccola"
HIT = "Colpo"
DAMAGE = "Danno"
USE = "Utilizza"
REROLL = "Rilancia"

HUMANOID = "Umanoide"

CANTRIPS = "Trucchetti"

SPELL_LIGHT = "Luce"

EVOCATION = "Evocazione"
TOUCH = "Tocco"
ACTION = "Azione"
CASTING_TIME = "Tempo di Lancio"
COMPONENTS = "Componenti"
DURATION = "Durata"
RANGE = "Gittata"

POISONING = "Avvelenamento"
MAGIC_SLEEP = "Addormentato dalla magia"
MAGIC_INTELLIGENCE = "Intelligenza (contro la magia)"
MAGIC_WISDOM = "Saggezza (contro la magia)"
MAGIC_CHARISMA = "Carisma (contro la magia)"
POISON = "Veleno"
FIRE = "Fuoco"
COLD = "Freddo"
LIGHTNING = "Fulmine"
ACID = "Acido"
NECROTIC = "Necrotico"
RADIANT = "Radiante"

WEIGHT_MULTIPLIER = 0.5 # lb->kg conversion. 1 lb = 0.5 kg. lb is default
LENGTH_MULTIPLIER = 0.3 # ft->m conversion. 1 ft = 0.3 m. ft is default
WEIGHT_UNIT = "kg"
LENGTH_UNIT = "m"

TIEFLING_VALUES = ["dell'Ambizione", "dell'Arte", "della Carogna", "del Canto", "del Credo", "della Morte", "della Dissolutezza", "della Disperazione", "della Condanna", "del Dubbio", "del Timore", "dell'Estasi", "dell'Anonimato", "dell'Entropia", "dell'Eccellenza", "della Paura", "della Gloria", "della Gola", "del Dolore", "dell'Odio", "della Speranza", "dell'Orrore", "dell'Ideale", "dell'Ignoto", "della Risata", "dell'Amore", "del Desiderio", "del Caos", "della Derisione", "dell'Omicidio", "della Musa", "della Musica", "del Mistero", "del Nulla", "dell'Apertura", "del Dolore", "della Passione", "della Poesia", "della Ricerca", "del Caso", "della Riverenza", "della Rivoltura", "del Cordoglio", "della Temerarietà", "del Tormento", "della Tragedia", "del Vizio", "della Virtù", "dell'Affaticamento", "dello Spirito"]

MISC_AASIMAR = ["Mani Guaritrici: come Magia, puoi toccare una creatura e tirare un numero di d4 pari al tuo Bonus di Competenza. La creatura recupera Punti Ferita pari al totale dei lanci. Può essere usato una volta per Riposo Lungo.", [3, "Rivelazione Celestiale", "Rivelazione Celestiale: come Azione Bonus puoi trasformarti usando una delle opzioni sottostanti (l'opzione può essere scelta ad ogni trasformazione). La trasformazione dura 1 minuto o fino a che non la interrompi (nessuna azione necessaria). Può essere usato una volta per Riposo Lungo.\nUna volta per turno prima che la trasformazione finisca, puoi infliggere danno extra ad un persaglio quando gli infliggi danno con un attacco od un incantesimo. Il danno extra equivale al tuo Bonus di Competenza ed è di tipo Necrotico per Manto Necrotico o Radiante per Ali Celestiali e Splendore Interno.\nLe opzioni di trasformazione sono le seguenti:\n - Ali Celestiali: due ali spettrali emergono temporaneamente dalla tua schiena. Fino alla fine della trasformazione, guadagni Velocità di Volo pari alla tua Velocità.\n - Splendore Interno: irradii temporaneamente una luce accecante da occhi e bocca. Per la durata della trasformazione, emetti Luce Luminosa in un raggio di 3 metri e Luce Fioca per altri 3 metri e, alla fine di ogni turno, ogni creatura entro 3 metri subisce danno Radiante pari al tuo Bonus di Competenza.\n - Manto Necrotico: i tuoi occhi divengono momentaneamente pozzi di oscurità e ali non adatte al volo ti emergono dalla schiena. Le creature non alleate entro 3 metri devono superare un tiro salvezza su Carisma (DC 8 più il tuo modificatore di Carisma ed il tuo Bonus di Competenza) o subire la condizione Spaventato fino alla fine del tuo turno successivo."]]
MISC_DRAGONBORN = ["Arma a Soffio: durante l'azione di Attacco nel tuo turno, puoi rimpiazzare uno dei tuoi attacchi con un soffio di fuoco. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo.", [5, "Volo Draconico", "Volo Draconico: come Azione Bonus puoi dar vita a ali spettrali sulla schiena che durano per 10 minuti a meno che tu non le ritragga (non serve un'azione) o tu divenga Incapacitato. Ottieni Velocità di Volo pari alla tua Velocità. Può essere usato una volta per Riposo Lungo."]]
MISC_ELF = ["Trance: non hai bisogno di dormire e non puoi essere addormentato dalla magia. Puoi finire un Riposo Lungo in 4 ore spendendo tali ore meditando in stato di trance, durante la quale mantieni coscienza."]
MISC_HALFLING = ["Agilità Halfling: puoi muoverti attraverso lo spazio di una creatura di taglia più grande, ma non puoi fermarti nello stesso spazio.", "Fortuna: quando tiri 1 sul d20 di una Prova D20, puoi tirare nuovamente il dado e devi usare il nuovo risultato", "Naturalmente Furtivo: puoi usare l'azione Nascondersi anche quando oscurato solamente da una creatura di taglia più grande."]
MISC_DWARF = ["Robustezza Nanica: i tuoi Punti Ferita massimi sono aumentati di 1 per ogni livello.", "Lavorazione della pietra: come Azione Bonus puoi ottenere Percezione Tellurica con raggio di 18m per 10 minuti. Può essere utilizzato se ti trovi su una superficie di pietra, naturale o lavorata, o se la stai toccando. Può essere usato un numero di volte pari al proprio Bonus di Competenza per Riposo Lungo."]
MISC_GNOME = []
MISC_GOLIATH = [[5, "Forma Larga", "Forma Larga: puoi cambiare la tua taglia in Grande come Azione Bonus se ti trovi in uno spazio sufficientemente grande. Questa trasformazione dura per 10 minuti o fino a quando non la interrompi (nessuna azione necessaria). Per tale periodo, ottieni Vantaggio alle prove su Forza, e la tua Velocità aumenta di 3 metri. Può essere usato una volta per Riposo Lungo."], "Costituzione Robusta: al fine di calcolare la capacità di carico, è come se fossi di una taglia più grande."]
MISC_TIEFLING = []
MISC_HUMAN = ["Ricco di Risorse: ottieni Ispirazione Eorica ogni volta che termini un Riposo Lungo."]

MISC_HIGH_ELF = ["Prestidigitazione: ad ogni Riposo Lungo, puoi rimpiazzare il tuo trucchetto di partenza (di base, Prestidigitazione), cun un diverso trucchetto preso dalla lista di incantesimi del Mago."]
MISC_FOREST_GNOME = ["Hai l'incantesimo Parlare con gli Animali sempre preparato. Puoi lanciarlo un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo. Puoi anche usare gli slot incantesimo per lanciarlo."]
MISC_ROCK_GNOME = ["Puoi dedicare 10 minuti al lancio di Prestidigitazione al fine di costruire un congegno meccanico Minuscolo (CA 5, 1 PF) come un giocattolo a molla, un accendifuoco od un carillon. Quando crei il congegno, ne determini la funzione scegliendo un effetto di Prestidigitazione; il congegno produce tale effetto quando tu od un'altra creatura utilizzate un'Azione Bonus per attivarlo toccandolo. Se l'effetto scelto ha varie opzioni, ne scegli una al momento della creazione. Per esempio, se scegli l'effetto di accensione/spegnimento di una fiamma della magia, è necessario determinare se il congegno accenda o spenga il fuoco; non può fare entrambe le cose. Puoi avere fino a tre congegni attivi contemporaneamente, ed ognuno di essi cade a pezzi 8 ore dopo la propria creazione o quando lo smantelli toccandolo con un Utilizzo."]
MISC_CLOUD_GIANT = ["Passeggiata tra le Nuvole: come Azione Bonus puoi teletrasportarti magicamente fino a 9 metri di distanza in uno spazio non occupato che puoi vedere. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo."]
MISC_FIRE_GIANT = ["Bruciatura del Fuoco: quando colpisci un bersaglio con un tiro per colpire e gli infliggi danno, puoi anche infliggere 1d10 danni di Fuoco a quel bersaglio. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo."]
MISC_FROST_GIANT = ["Brivido del Gelo: quando colpisci un bersaglio con un tiro per colpire e gli infliggi danno, puoi anche infliggere 1d6 danni di Gelo a quel bersaglio e ridurre la sua Velocità di 3 metri fino alla fine del tuo prossimo turno. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo."]
MISC_HILL_GIANT = ["Caduta della Collina: quando colpisci una creatura Grande o più piccola con un tiro per colpire e le infliggi danno, puoi infliggere a tale bersaglio la condizione Prono. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo."]
MISC_STONE_GIANT = ["Resistenza della pietra: quando subisci danno, puoi utilizzare una Reazione per tirare 1d12. Riduci il danno subito di tale risultato sommato al tuo modificatore di Costituzione. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo."]
MISC_STORM_GIANT = ["Tuono della Tempesta: quando subisci danno da una creatura entro 18 piedi, puoi usare una Reazione per infliggere 1d8 danni da Tuono a tale creatura. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Lungo."]
MISC_ORC = ["Scarica di Adrenalina: puoi utilizzare l'azione Scatto come Azione Bonus. Quando lo fai, ottieni un quantitativo di Punti Ferita Temporanei pari al tuo Bonus di Competenza. Può essere usato un numero di volte pari al tuo Bonus di Competenza per Riposo Breve o Riposo Lungo.", "Resistenza Inflessibile: quando sei ridotto a 0 Punti Ferita senza essere ucciso sul colpo, puoi invece restare a 1 Punto Ferita. Può essere usato una volta per Riposo Lungo."]

MISC_BARBARIAN = ["Ira: puoi permearti di un potere primordiale chiamato Ira, una forza che ti conferisce straordinari potere e resilienza. Puoi entrarvi come Azione Bonus se non indossi un'Armatura Pesante.\nIl numero di utilizzi dell'Ira dipende dal tuo livello di Barbaro. Recuperi un utilizzo speso quando termini un Riposo Breve e recuperi tutti gli utilizzi spesi quando termini un Riposo Lungo.\nMentre è attiva, la tua Ira segue le seguenti regole.\nReistenza al Danno: hai Resistenza ai danni Contundenti, Perforanti e Taglienti.\nDanno Iracondo: quando attacchi utilizzando Forza - con un'arma od un Attacco senza Armi - e infliggi danno al bersaglio, ricevi un bonus al danno che aumenta insieme ai livelli di Barbaro.\nVantaggio in Forza: hai Vantaggio alle prove di Forza ed ai tiri salvezza su Forza.\nNiente Concentrazione o Incantesimi: non puoi mantenere Concentrazione e non puoi lanciare incantesimi.\nDurata: l'Ira dura fino alla fine del tuo prossimo turno e finisce anticipatamente se indossi Armatura Pesante o subisci la condizione Incapacitato. Se la tua Ira è ancora attiva durante il tuo prossimo turno, puoi estenderla per un altro round facendo una delle azioni seguenti:\n - Fai un tiro di attacco contro un nemico.\n - Fai fare un tiro di salvezza ad un nemico.\n - Usa un'Azione Bonus per estendere la tua Ira.\nOgni volta che l'Ira viene estesa, dura fino alla fine del tuo turno successivo. Puoi mantenere l'Ira per un massimo di 10 minuti.", "Difesa senza Armatura: quando non indossi alcuna armatura, la tua Classe Armatura base è pari a 10 più i tuoi modificatori di Destrezza e Costituzione. Ottieni questo beneficio anche equipaggiando uno scudo."]

MISC_ALERT = ["Scambio di Iniziativa: subito dopo aver tirato la tua Iniziativa, puoi scambiare tale Iniziativa con l'Iniziativa di un alleato consenziente che partecipa allo stesso combattimento. Non puoi fare tale scambio se tu o il tuo alleato avete la condizione Incapacitato."]
MISC_CRAFTER = ["Sconto: quando acquisti un oggetto non magico, ottieni 20 per cento di sconto.", "Costruzione Veloce: quando finisci un Riposo Lungo, puoi costruire un pezzo di equipaggiamento costruibile con uno Strumento da Artigiano che possiedi e con cui hai competenza. L'oggetto dura fino a che non finisci un altro Riposo Lungo, dopo di che cade a pezzi."]
MISC_HEALER = ["Medico da Combattimento: se hai un'Equipaggiamento da Medico, puoi spenderne un utilizzo per curare una creatura entro 1,5 metri da te con un Utilizzo. Quella creatura può spendere uno dei propri Dadi Vita che tu tirerai. La creatura recupera un numero di Punti Ferita pari al risultato del tiro più il tuo Bonus di Competenza.", "Rilanci di Cura: quando tiri un dado per determinare il numero di Punti Ferita ripristinati da un incantesimo o con il beneficio Medico da Battaglia di questo talento, puoi tirare nuovamente il dado se il risultato è 1, devi usare il nuovo risultato."]
MISC_LUCKY = ["Punti Fortuna: possiedi un numero di Punti Fortuna pari al tuo Bonus di Competenza e puoi spenderli nei benefici seguenti. Recuperi i Punti Viti spesi quando termini un Riposo Lungo.", "Vantaggio: quando tiri un d20 per un Test D20, puoi spendere un Punto Fortuna per darti Vantaggio al tiro.", "Svantaggio: quando una creatura tira un d20 per attaccarti, puoi spnedere 1 Punto Fortuna per imporre Svantaggio a quel tiro."]
MISC_MAGIC_INITIATE = ["Cambio di Incantesimo: quando sali di livello, puoi cambiare uno degli incantesimi scelti per questo talento con un incantesimo differente dello stesso livello dalla lista incantesimi scelta."]
MISC_MUSICIAN = ["Canzone Incoraggiante: quando finisci un Riposo Breve od un Riposo Lungo, puoi suonare un brano con uno Strumento Musicale col quale hai Competenza e dare Ispirazione Eroica agli alleati che sentono la canzone. Il numero di alleati che puoi influenzare in questa maniera equivale al tuo Bonus di Competenza."]
MISC_SAVAGE_ATTACKER = ["Una volta per turno, quando colpisci un bersaglio con un'arma, puoi tirare il dado dei danni due volte e scegliere quale risultato usare contro il bersaglio."]
MISC_SKILLED = []
MISC_TAVERN_BRAWLER = ["Attacco senza Armi Migliorato: quando colpisci ocn il tuo Attacco senza Armi e infliggi danno, puoi infliggere danno Contundente pari a 1d4 più il tuo modificatore di Forza invece del tipico danno di un Attacco senza Armi.", "Rilanci di Danno: quando lanci un dado di danno per il tuo Attacco senza Armi, puoi tirare nuovamente il dado se il risultato è 1, devi usare il nuovo risultato.", "Armamentario Improvvisato: hai competenza nelle Armi Improvvisate.", "Spinta: quando colpisci una creatura con un Attacco senza Armi come parte dell'azione di Attacco durante il tuo turno, puoi infliggere danno al bersaglio e, inoltre, spingerlo lontano da te di 1,5 metri. Può essere usato una volta per turno."]
MISC_TOUGH = ["I tuoi Punti Ferita massimi sono aumentati di 2 per ogni livello."]

# Constants for graphical rendering

LEN_FEATS = 7 # Maximum number of feats
LEN_FEATURES = LEN_FEATS + 2 # Features can come from feats, species or class
CLASS_FEATURES_INDEX = LEN_FEATS + 1

OFFSET = 2
MAX_NUM = 2
MAX_HEIGHT = OFFSET * MAX_NUM

# Constants depending on the setting

# Used to identify which equipment to give to the character
EQUIPMENTS_BASE_BACKGROUND = {
    ACOLYTE_EQUIPMENT: ACOLYTE,
    ARTISAN_EQUIPMENT: ARTISAN,
    CHARLATAN_EQUIPMENT: CHARLATAN,
    CRIMINAL_EQUIPMENT: CRIMINAL,
    ENTERTAINER_EQUIPMENT: ENTERTAINER,
    FARMER_EQUIPMENT: FARMER,
    GUARD_EQUIPMENT: GUARD,
    GUIDE_EQUIPMENT: GUIDE,
    HERMIT_EQUIPMENT: HERMIT,
    MERCHANT_EQUIPMENT: MERCHANT,
    NOBLE_EQUIPMENT: NOBLE,
    SAGE_EQUIPMENT: SAGE,
    SAILOR_EQUIPMENT: SAILOR,
    SCRIBE_EQUIPMENT: SCRIBE,
    SOLDIER_EQUIPMENT: SOLDIER,
    WAYFARER_EQUIPMENT: WAYFARER
}

EQUIPMENTS_BASE_CLASS = {
    BARBARIAN_EQUIPMENT: BARBARIAN,
    BARD_EQUIPMENT: BARD,
    CLERIC_EQUIPMENT: CLERIC,
    DRUID_EQUIPMENT: DRUID,
    FIGHTER_EQUIPMENT: FIGHTER,
    ROGUE_EQUIPMENT: ROGUE,
    WIZARD_EQUIPMENT: WIZARD,
    MONK_EQUIPMENT: MONK,
    PALADIN_EQUIPMENT: PALADIN,
    RANGER_EQUIPMENT: RANGER,
    SORCERER_EQUIPMENT: SORCERER,
    WARLOCK_EQUIPMENT: WARLOCK
}

WEAPON_MASTERIES = [CLEAVE, GRAZE, NICK, PUSH, SAP, SLOW, TOPPLE, VEX]

FAST_CRAFTING = {
    CARPENTERS_TOOLS: [LADDER, TORCH],
    LEATHERWORKERS_TOOLS: [CASE, POUCH],
    MASONS_TOOLS: [BLOCK_AND_TACKLE],
    POTTERS_TOOLS: [JUG, LAMP],
    SMITHS_TOOLS: [BALL_BEARINGS, BUCKET, CALTROPS, GRAPPLING_HOOK, IRON_POT],
    TINKERS_TOOLS: [BELL, SHOVEL, TINDER_BOX],
    WEAVERS_TOOLS: [BASKET, ROPE, NET, TENT],
    WOODCARVERS_TOOLS: [CLUB, GREATCLUB, QUARTERSTAFF]
}

STANDARD_LANGUAGES = [COMMON_LANGUAGE, COMMON_SIGN_LANGUAGE, ELVISH_LANGUAGE, DRACONIC_LANGUAGE, DWARVISH_LANGUAGE, HALFLING_LANGUAGE, GNOMISH_LANGUAGE, ORC_LANGUAGE, GOBLIN_LANGUAGE, GIANT_LANGUAGE]
RARE_LANGUAGES = [CELESTIAL_LANGUAGE, ABYSSAL_LANGUAGE, INFERNAL_LANGUAGE, DEEP_SPEECH, PRIMORDIAL_LANGUAGE, SYLVAN_LANGUAGE, UNDERCOMMON_LANGUAGE, DRUIDIC_LANGUAGE, THIEVES_CANT]

# The character's info is stored into a dictionary. These are the default values
DEFAULT_CHARACTER_INFO = {
    STRENGTH: None,
    DEXTERITY: None,
    CONSTITUTION: None,
    INTELLIGENCE: None,
    WISDOM: None,
    CHARISMA: None,
    "feats": [RANDOM] * LEN_FEATS,
    "feats1": [RANDOM] * LEN_FEATS,
    "feats2": [RANDOM] * LEN_FEATS,
    "species": RANDOM,
    "lineage": RANDOM,
    "alignment": RANDOM,
    "background": RANDOM,
    "class": RANDOM,
    "subclass": RANDOM,
    "level": RANDOM,
    "age": None,
    "ageMin": None,
    "ageMax": None,
    "height": None,
    "weight": None,
    "gender": RANDOM,
    "name": None,
    "health": None,
    "healthCurrent": None,
    "healthFixed": False,
    "equipmentFixed": False,
    ACROBATICS: False,
    ANIMAL_HANDLING: False,
    ARCANA: False,
    ATHLETICS: False,
    DECEPTION: False,
    HISTORY: False,
    INSIGHT: False,
    INTIMIDATION: False,
    INVESTIGATION: False,
    MEDICINE: False,
    NATURE: False,
    PERCEPTION: False,
    PERFORMANCE: False,
    PERSUASION: False,
    RELIGION: False,
    SLEIGHT_OF_HAND: False,
    STEALTH: False,
    SURVIVAL: False,
    f"{STRENGTH}1": False,
    f"{DEXTERITY}1": False,
    f"{CONSTITUTION}1": False,
    f"{INTELLIGENCE}1": False,
    f"{WISDOM}1": False,
    f"{CHARISMA}1": False,
    f"{STRENGTH}2": False,
    f"{DEXTERITY}2": False,
    f"{CONSTITUTION}2": False,
    f"{INTELLIGENCE}2": False,
    f"{WISDOM}2": False,
    f"{CHARISMA}2": False,
    "savename": "",
    "languages": [],
    "proficiencies": [],
    "equipment": {},
    "equipmentBase": [[], []],
    "mainHand": None,
    "offHand": None,
    "armor": None,
    "available languages": [RANDOM] + STANDARD_LANGUAGES,
    "finesse": HIGHEST,
    "hitDie": None,
    "points": [""] * LEN_FEATURES * MAX_NUM,
    "copper": None,
    "silver": None,
    "electrum": None,
    "gold": None,
    "platinum": None,
    BLINDED: False,
    CHARMED: False,
    DEAFENED: False,
    EXHAUSTION: 0,
    FRIGHTENED: False,
    GRAPPLED: False,
    INCAPACITATED: False,
    INVISIBLE: False,
    PARALYZED: False,
    PETRIFIED: False,
    POISONED: False,
    PRONE: False,
    RESTRAINED: False,
    STUNNED: False,
    UNCONSCIOUS: False,
    "incapacitated counter": 0
    }

ABILITIES = [STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA]
ABILITIES_CHOICES = [RANDOM] + ABILITIES # The actual available choices when creating a character
SKILLS = {
    ACROBATICS: DEXTERITY, # Each skill also lists which ability it's based on
    ANIMAL_HANDLING: WISDOM,
    ARCANA: INTELLIGENCE,
    ATHLETICS: STRENGTH,
    DECEPTION: CHARISMA,
    HISTORY: INTELLIGENCE,
    INSIGHT: WISDOM,
    INTIMIDATION: CHARISMA,
    INVESTIGATION: INTELLIGENCE,
    MEDICINE: WISDOM,
    NATURE: INTELLIGENCE,
    PERCEPTION: WISDOM,
    PERFORMANCE: CHARISMA,
    PERSUASION: CHARISMA,
    RELIGION: INTELLIGENCE,
    SLEIGHT_OF_HAND: DEXTERITY,
    STEALTH: DEXTERITY,
    SURVIVAL: WISDOM
}
WEIGHT_GENDER = 1.2 # Weight depends on gender. Males are 1.2 times heavier than females
HEIGHT_GENDER = 1.07 # Height depends on gender

WEAPONS: Dict[str, Dict[str, Union[str, int, list]]] = {
    UNARMED_STRIKE: {"cost": "", "damage": "1d1", "type": BLUDGEONING, "mastery": None, "weight": 0, "properties": [], "tags": []},
    IMPROVISED_WEAPON: {"cost": "", "damage": "1d4", "type": BLUDGEONING, "mastery": None, "weight": 0, "properties": [THROWN], "range": [20, 60], "tags": []},

    CLUB: {"cost": f"1 {GOLD}", "damage": "1d4", "type": BLUDGEONING, "mastery": SLOW, "weight": 2, "properties": [LIGHT], "tags": [SIMPLE, MELEE]},
    DAGGER: {"cost": f"2 {GOLD}", "damage": "1d4", "type": PIERCING, "mastery": NICK, "weight": 1, "properties": [LIGHT, FINESSE, THROWN], "range": [20, 60], "tags": [SIMPLE, MELEE]},
    GREATCLUB: {"cost": f"2 {SILVER}", "damage": "1d8", "type": BLUDGEONING, "mastery": PUSH, "weight": 10, "properties": [TWO_HANDED], "tags": [SIMPLE, MELEE]},
    HANDAXE: {"cost": f"5 {GOLD}", "damage": "1d6", "type": SLASHING, "mastery": VEX, "weight": 2, "properties": [LIGHT, THROWN], "range": [20, 60], "tags": [SIMPLE, MELEE]},
    JAVELIN: {"cost": f"5 {SILVER}", "damage": "1d6", "type": PIERCING, "mastery": SLOW, "weight": 2, "properties": [THROWN], "range": [30, 120], "tags": [SIMPLE, MELEE]},
    LIGHT_HAMMER: {"cost": f"2 {GOLD}", "damage": "1d4", "type": BLUDGEONING, "mastery": NICK, "weight": 2, "properties": [LIGHT, THROWN], "range": [20, 60], "tags": [SIMPLE, MELEE]},
    MACE: {"cost": f"5 {GOLD}", "damage": "1d6", "type": BLUDGEONING, "mastery": SAP, "weight": 4, "properties": [], "tags": [SIMPLE, MELEE]},
    QUARTERSTAFF: {"cost": f"2 {SILVER}", "damage": "1d6", "type": BLUDGEONING, "mastery": TOPPLE, "weight": 4, "properties": [VERSATILE], "damage versatile": "1d8", "tags": [SIMPLE, MELEE]},
    SICKLE: {"cost": f"1 {GOLD}", "damage": "1d4", "type": SLASHING, "mastery": NICK, "weight": 2, "properties": [LIGHT], "tags": [SIMPLE, MELEE]},
    SPEAR: {"cost": f"1 {GOLD}", "damage": "1d6", "type": PIERCING, "mastery": SAP, "weight": 3, "properties": [THROWN, VERSATILE], "damage versatile": "1d8", "tags": [SIMPLE, MELEE]},
    DART: {"cost": f"5 {SILVER}", "damage": "1d4", "type": PIERCING, "mastery": VEX, "weight": 0.25, "properties": [FINESSE, THROWN], "range": [20, 60], "tags": [SIMPLE, RANGED]},
    CROSSBOW_LIGHT: {"cost": f"25 {GOLD}", "damage": "1d8", "type": PIERCING, "mastery": SLOW, "weight": 5, "properties": [AMMUNITION, LOADING, TWO_HANDED], "range": [80, 320], "ammo": BOLT, "tags": [SIMPLE, RANGED]},
    SHORTBOW: {"cost": f"25 {GOLD}", "damage": "1d6", "type": PIERCING, "mastery": VEX, "weight": 2, "properties": [AMMUNITION, TWO_HANDED], "range": [80, 320], "ammo": ARROW, "tags": [SIMPLE, RANGED]},
    SLING: {"cost": f"1 {SILVER}", "damage": "1d4", "type": BLUDGEONING, "mastery": SLOW, "weight": 0, "properties": [AMMUNITION], "range": [30, 120], "ammo": BULLET, "tags": [SIMPLE, RANGED]},
    BATTLEAXE: {"cost": f"10 {GOLD}", "damage": "1d8", "type": SLASHING, "mastery": TOPPLE, "weight": 4, "properties": [VERSATILE], "damage versatile": "1d10", "tags": [MARTIAL, MELEE]},
    FLAIL: {"cost": f"10 {GOLD}", "damage": "1d8", "type": BLUDGEONING, "mastery": SAP, "weight": 2, "properties": [], "tags": [MARTIAL, MELEE]},
    GLAIVE: {"cost": f"20 {GOLD}", "damage": "1d10", "type": SLASHING, "mastery": GRAZE, "weight": 6, "properties": [HEAVY, REACH, TWO_HANDED], "tags": [MARTIAL, MELEE]},
    GREATAXE: {"cost": f"30 {GOLD}", "damage": "1d12", "type": SLASHING, "mastery": CLEAVE, "weight": 7, "properties": [HEAVY, TWO_HANDED], "tags": [MARTIAL, MELEE]},
    GREATSWORD: {"cost": f"50 {GOLD}", "damage": "2d6", "type": SLASHING, "mastery": GRAZE, "weight": 6, "properties": [HEAVY, TWO_HANDED], "tags": [MARTIAL, MELEE]},
    HALBERD: {"cost": f"20 {GOLD}", "damage": "1d10", "type": SLASHING, "mastery": CLEAVE, "weight": 6, "properties": [HEAVY, REACH, TWO_HANDED], "tags": [MARTIAL, MELEE]},
    LANCE: {"cost": f"10 {GOLD}", "damage": "1d12", "type": PIERCING, "mastery": TOPPLE, "weight": 6, "properties": [REACH], "tags": [MARTIAL, MELEE]},
    LONGSWORD: {"cost": f"15 {GOLD}", "damage": "1d8", "type": SLASHING, "mastery": SAP, "weight": 3, "properties": [VERSATILE], "damage versatile": "1d10", "tags": [MARTIAL, MELEE]},
    MAUL: {"cost": f"10 {GOLD}", "damage": "2d6", "type": BLUDGEONING, "mastery": TOPPLE, "weight": 10, "properties": [HEAVY, TWO_HANDED], "tags": [MARTIAL, MELEE]},
    MORNINGSTAR: {"cost": f"15 {GOLD}", "damage": "1d8", "type": PIERCING, "mastery": SAP, "weight": 4, "properties": [], "tags": [MARTIAL, MELEE]},
    PIKE: {"cost": f"5 {GOLD}", "damage": "1d10", "type": PIERCING, "mastery": PUSH, "weight": 18, "properties": [HEAVY, REACH, TWO_HANDED], "tags": [MARTIAL, MELEE]},
    RAPIER: {"cost": f"25 {GOLD}", "damage": "1d8", "type": PIERCING, "mastery": VEX, "weight": 2, "properties": [FINESSE], "tags": [MARTIAL, MELEE]},
    SCIMITAR: {"cost": f"25 {GOLD}", "damage": "1d6", "type": SLASHING, "mastery": NICK, "weight": 3, "properties": [FINESSE, LIGHT], "tags": [MARTIAL, MELEE]},
    SHORTSWORD: {"cost": f"10 {GOLD}", "damage": "1d6", "type": PIERCING, "mastery": VEX, "weight": 2, "properties": [FINESSE, LIGHT], "tags": [MARTIAL, MELEE]},
    TRIDENT: {"cost": f"5 {GOLD}", "damage": "1d6", "type": PIERCING, "mastery": TOPPLE, "weight": 4, "properties": [THROWN, VERSATILE], "range": [20, 60], "damage versatile": "1d8", "tags": [MARTIAL, MELEE]},
    WARHAMMER: {"cost": f"15 {GOLD}", "damage": "1d8", "type": BLUDGEONING, "mastery": PUSH, "weight": 2, "properties": [VERSATILE], "damage versatile": "1d10", "tags": [MARTIAL, MELEE]},
    WAR_PICK: {"cost": f"5 {GOLD}", "damage": "1d8", "type": PIERCING, "mastery": SAP, "weight": 2, "properties": [], "tags": [MARTIAL, MELEE]},
    WHIP: {"cost": f"2 {GOLD}", "damage": "1d4", "type": SLASHING, "mastery": SLOW, "weight": 3, "properties": [REACH], "tags": [MARTIAL, MELEE]},
    BLOWGUN: {"cost": f"10 {GOLD}", "damage": "1", "type": PIERCING, "mastery": VEX, "weight": 1, "properties": [AMMUNITION, LOADING], "range": [25, 100], "ammo": NEEDLE, "tags": [MARTIAL, RANGED]},
    CROSSBOW_HAND: {"cost": f"75 {GOLD}", "damage": "1d6", "type": PIERCING, "mastery": VEX, "weight": 3, "properties": [AMMUNITION, LIGHT, LOADING], "range": [30, 120], "ammo": BOLT, "tags": [MARTIAL, RANGED]},
    CROSSBOW_HEAVY: {"cost": f"50 {GOLD}", "damage": "1d10", "type": PIERCING, "mastery": PUSH, "weight": 18, "properties": [AMMUNITION, HEAVY, LOADING, TWO_HANDED], "range": [100, 400], "ammo": BOLT, "tags": [MARTIAL, RANGED]},
    LONGBOW: {"cost": f"50 {GOLD}", "damage": "1d8", "type": PIERCING, "mastery": SLOW, "weight": 2, "properties": [AMMUNITION, HEAVY, TWO_HANDED], "range": [150, 600], "ammo": ARROW, "tags": [MARTIAL, RANGED]},
    MUSKET: {"cost": f"500 {GOLD}", "damage": "1d12", "type": PIERCING, "mastery": SLOW, "weight": 10, "properties": [AMMUNITION, LOADING, TWO_HANDED], "range": [40, 120], "ammo": BULLET, "tags": [MARTIAL, RANGED]},
    PISTOL: {"cost": f"250 {GOLD}", "damage": "1d10", "type": PIERCING, "mastery": VEX, "weight": 3, "properties": [AMMUNITION, LOADING], "range": [30, 90], "ammo": BULLET, "tags": [MARTIAL, RANGED]},
}

SIMPLE_WEAPONS = [weapon for weapon in WEAPONS if SIMPLE in WEAPONS[weapon]["tags"]]
MARTIAL_WEAPONS = [weapon for weapon in WEAPONS if MARTIAL in WEAPONS[weapon]["tags"]]
MELEE_WEAPONS = [weapon for weapon in WEAPONS if MELEE in WEAPONS[weapon]["tags"]]
RANGED_WEAPONS = [weapon for weapon in WEAPONS if RANGED in WEAPONS[weapon]["tags"]]
SIMPLE_MELEE_WEAPONS = [weapon for weapon in SIMPLE_WEAPONS if MELEE in WEAPONS[weapon]["tags"] and SIMPLE in WEAPONS[weapon]["tags"]]
SIMPLE_RANGED_WEAPONS = [weapon for weapon in SIMPLE_WEAPONS if RANGED in WEAPONS[weapon]["tags"] and SIMPLE in WEAPONS[weapon]["tags"]]
MARTIAL_MELEE_WEAPONS = [weapon for weapon in MARTIAL_WEAPONS if MELEE in WEAPONS[weapon]["tags"] and MARTIAL in WEAPONS[weapon]["tags"]]
MARTIAL_RANGED_WEAPONS = [weapon for weapon in MARTIAL_WEAPONS if RANGED in WEAPONS[weapon]["tags"] and MARTIAL in WEAPONS[weapon]["tags"]]

ARMORS =  {
    PADDED_ARMOR: {"cost": f"5 {GOLD}", "armor class": 11, "dexterity": -1, "strength": 0, "stealth": True, "weight": 8, "tags": [LIGHT]},
    LEATHER_ARMOR: {"cost": f"10 {GOLD}", "armor class": 11, "dexterity": -1, "strength": 0, "stealth": False, "weight": 10, "tags": [LIGHT]},
    STUDDED_LEATHER_ARMOR: {"cost": f"45 {GOLD}", "armor class": 12, "dexterity": -1, "strength": 0, "stealth": False, "weight": 13, "tags": [LIGHT]},
    HIDE_ARMOR: {"cost": f"10 {GOLD}", "armor class": 12, "dexterity": 2, "strength": 0, "stealth": False, "weight": 12, "tags": [MEDIUM]},
    CHAIN_SHIRT: {"cost": f"50 {GOLD}", "armor class": 13, "dexterity": 2, "strength": 0, "stealth": False, "weight": 20, "tags": [MEDIUM]},
    SCALE_MAIL: {"cost": f"50 {GOLD}", "armor class": 14, "dexterity": 2, "strength": 0, "stealth": True, "weight": 45, "tags": [MEDIUM]},
    BREASTPLATE: {"cost": f"400 {GOLD}", "armor class": 14, "dexterity": 2, "strength": 0, "stealth": False, "weight": 20, "tags": [MEDIUM]},
    HALF_PLATE: {"cost": f"750 {GOLD}", "armor class": 15, "dexterity": 2, "strength": 0, "stealth": True, "weight": 40, "tags": [HEAVY]},
    RING_MAIL: {"cost": f"30 {GOLD}", "armor class": 14, "dexterity": 0, "strength": 0, "stealth": True, "weight": 40, "tags": [HEAVY]},
    CHAIN_MAIL: {"cost": f"75 {GOLD}", "armor class": 16, "dexterity": 0, "strength": 13, "stealth": True, "weight": 55, "tags": [HEAVY]},
    SPLINT_ARMOR: {"cost": f"200 {GOLD}", "armor class": 17, "dexterity": 0, "strength": 15, "stealth": True, "weight": 60, "tags": [HEAVY]},
    PLATE_ARMOR: {"cost": f"1500 {GOLD}", "armor class": 18, "dexterity": 0, "strength": 15, "stealth": True, "weight": 65, "tags": [HEAVY]},
    SHIELD: {"cost": f"10 {GOLD}", "armor class": 2, "strength": 0, "dexterity": 0, "stealth": False, "weight": 6, "tags": [SHIELDS_TRANSLATE]}
}

LIGHT_ARMORS = [armor for armor in ARMORS if LIGHT in ARMORS[armor]["tags"]]
MEDIUM_ARMORS = [armor for armor in ARMORS if MEDIUM in ARMORS[armor]["tags"]]
HEAVY_ARMORS = [armor for armor in ARMORS if HEAVY in ARMORS[armor]["tags"]]
SHIELDS = [armor for armor in ARMORS if SHIELDS_TRANSLATE in ARMORS[armor]["tags"]]

WEAPON_TYPES = {
    BATTLEAXES: [BATTLEAXE], # In case future weapons that cathegorize as battleaxes are added
    HANDAXES: [HANDAXE],
    LIGHT_HAMMERS: [LIGHT_HAMMER],
    WARHAMMERS: [WARHAMMER],
    SHORTSWORDS: [SHORTSWORD],
    LONGSWORDS: [LONGSWORD],
    SHORTBOWS: [SHORTBOW],
    LONGBOWS: [LONGBOW],
    RAPIERS: [RAPIER],
    CROSSBOW_HANDS: [CROSSBOW_HAND],
    LIGHT_ARMORS_TRANSLATE: LIGHT_ARMORS,
    MEDIUM_ARMORS_TRANSLATE: MEDIUM_ARMORS,
    HEAVY_ARMORS_TRANSLATE: HEAVY_ARMORS,
    SHIELDS_TRANSLATE: SHIELDS,
    SIMPLE_WEAPONS_TRANSLATE: SIMPLE_WEAPONS,
    MARTIAL_WEAPONS_TRANSLATE: MARTIAL_WEAPONS
}

ADVENTURING_GEAR = {
    HEALERS_KIT: {"cost": f"5 {GOLD}", "weight": 3, "uses": 10}
}

ARTISANS_TOOLS = [RANDOM, SMITHS_TOOLS, BREWERS_SUPPLIES, MASONRY_TOOLS]
GAMING_SETS = [RANDOM, DICE, DRAGONCHESS, PLAYING_CARDS, THREE_DRAGON_ANTE]
MUSICAL_INSTRUMENTS = [RANDOM, BAGPIPES, DRUM, DULCIMER, FLUTE, HORN, LUTE, LYRE, PAN_FLUTE, SHAWN, VIOL]


FEATS: Dict[str, Dict] = {
    RANDOM: {
        "type": NONE_M,
        "repeatable": True
    },
    f"{ABILITY_SCORE_INCREASE} +2": {
        "probability": 1,
        "bonus ability 2": [1, 1, 1, 1, 1, 1],
        "type": GENERAL_FEAT,
        "repeatable": True
    },
    f"{ABILITY_SCORE_INCREASE} +1/+1": {
        "probability": 1,
        "bonus ability 1": [2, 2, 2, 2, 2, 2],
        "type": GENERAL_FEAT,
        "repeatable": True
    },
    ALERT: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_ALERT,
        "initiative proficiency": True
    },
    CRAFTER: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_CRAFTER,
        "tool proficiencies": [[RANDOM] + list(FAST_CRAFTING.keys()) + list(FAST_CRAFTING.keys()) + list(FAST_CRAFTING.keys())],
        "features": [QUICK_CRAFTING]
    },
    HEALER: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_HEALER,
        "features": [BATTLE_MEDIC]
    },
    LUCKY: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_LUCKY,
        "features": [LUCK_POINTS]
    },
    MAGIC_INITIATE: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": True,
        "misc": MISC_MAGIC_INITIATE
    },
    MUSICIAN: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_MUSICIAN
    },
    SAVAGE_ATTACKER: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_SAVAGE_ATTACKER
    },
    SKILLED: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": True,
        "misc": MISC_SKILLED
    },
    TAVERN_BRAWLER: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_TAVERN_BRAWLER
    },
    TOUGH: {
        "probability": 1,
        "type": ORIGIN_FEAT,
        "repeatable": False,
        "misc": MISC_TOUGH
    }
}

ORIGIN_FEATS = [RANDOM] + [feat for feat in FEATS if FEATS[feat]["type"] == ORIGIN_FEAT]

FEAT_WEIGHTS = {feat: info["probability"] for feat, info in FEATS.items() if "probability" in info}


SPECIES: Dict[str, Dict[str, Dict[str, Dict]]] = {
    RANDOM: {
        "lineages": {
            RANDOM: {

            },
            NONE_F: {
                "probability": 1
            }
        }
    },

    AASIMAR: {
        "probability": 1,
        "type:": HUMANOID,
        "age max": 160,
        "languages": [COMMON_LANGUAGE, CELESTIAL_LANGUAGE, RANDOM_STANDARD],

        "misc": MISC_AASIMAR,
        "features": [HEALING_HANDS, [3, CELESTIAL_REVELATION]],
        "speed": 30,
        "darkvision": 60,
        "resistances": [NECROTIC, RADIANT],
        "magic": [[SPELL_LIGHT, CHARISMA]],
        "lineages": {
            
        }
    },

    DRAGONBORN: {
        "probability": 1,
        "probability alignment 1": [45, 10, 45],
        "probability alignment 2": [3, 1, 6],
        "health base": 4,
        "age max": 80,
        "development age": 15,
        "size": MEDIUM,
        "type:": HUMANOID,
        "base height": 5 + 6/12,
        "height modifier": "2d8",
        "base weight": 175,
        "weight modifier": "2d6",
        "languages": [COMMON_LANGUAGE, DRACONIC_LANGUAGE, RANDOM_STANDARD],
        "male names": ["Adrex", "Arjhan", "Azzakh", "Balasar", "Baradad", "Bharash", "Bidreked", "Dadalan", "Dazzazn", "Direcris", "Donaar", "Fax", "Gargax", "Ghesh", "Gorbundus", "Greethen", "Heskan", "Hirrathak", "Ildrex", "Kaladan", "Kerkad", "Kiirith", "Kriv", "Maagog", "Medrash", "Mehen", "Mozikth", "Mreksh", "Mugrunden", "Nadarr", "Nithther", "Norkruuth", "Nykkan", "Pandjed", "Patrin", "Pijjirik", "Quarethon", "Rathkran", "Rhogar", "Rivaan", "Sethrekar", "Shamash", "Shedinn", "Srorthen", "Tarhun", "Torinn", "Trynnicus", "Valorean", "Vrondiss", "Zedaar"],
        "female names": ["Akra", "Aasathra", "Antrara", "Arava", "Biri", "Blendaeth", "Burana", "Chassath", "Daar", "Dentratha", "Doudra", "Driindar", "Eggren", "Farideh", "Findex", "Furrele", "Gesrethe", "Gilkass", "Harann", "Havilar", "Hethress", "Hillanot", "Jaxi", "Jezean", "Jheri", "Kadana", "Kava", "Korinn", "Megren", "Mijira", "Mishann", "Nala", "Nuthra", "Perra", "Pogranix", "Pyxrin", "Quespa", "Raiann", "Rezena", "Ruloth", "Saphara", "Savaran", "Sora", "Surina", "Synthrin", "Tatyan", "Thava", "Uadjit", "Vezera", "Zykroff"],
        "last names": ["Akambherylliax", "Argenthrixus", "Baharoosh", "Beryntolthropal", "Bhenkumbyrznaax", "Caavylteradyn", "Chumbyxirinnish", "Clethtinthiallor", "Daardendrian", "Delmirev", "Dhyrktelonis", "Ebynichtomonis", "Esstyrlynn", "Fharngnarthnost", "Ghaallixirn", "Grrrmmballhyst", "Gygazzylyshrift", "Hashphronyxadyn", "Hshhsstoroth", "Imbixtellrhyst", "Jerynomonis", "Jharthraxyn", "Kerrhylon", "Kimbatuul", "Lhamboldennish", "Linxakasendalor", "Mohradyllion", "Mystan", "Nemmonis", "Norixius", "Ophinshtalajiir", "Orexijandilin", "Pfaphnyrennish", "Phrahdrandon", "Pyraxtallinost", "Qyxpahrgh", "Raghthroknaar", "Shestendeliath", "Skaarzborroosh", "Sumnarghthrysh", "Tiammanthyllish", "Turnuroth", "Umbyrphrael", "Vangdondalor", "Verthisathurgiesh", "Wivvyrholdalphiax", "Wystongjiir", "Xephyrbahnor", "Yarjerit", "Zzzxaaxthroth"],
        
        "misc": MISC_DRAGONBORN,
        "speed": 30,
        "darkvision": 60,
        "lineages": {
            RANDOM: {

            },
            SILVER_DRAGONBORN: {
                "probability": 1,
                "probability alignment 1": [45, 10, 45],
                "resistances": [COLD],
                "magic": [["PLACEHOLDER"]]
            },
            WHITE_DRAGONBORN: {
                "probability": 1,
                "resistances": [COLD],
                "magic": [["PLACEHOLDER"]]      
            },
            BLUE_DRAGONBORN: {
                "probability": 1,
                "resistances": [LIGHTNING],
                "magic": [["PLACEHOLDER"]]
            },
            BRONZE_DRAGONBORN: {
                "probability": 1,
                "resistances": [LIGHTNING],
                "magic": [["PLACEHOLDER"]]
            },
            BLACK_DRAGONBORN: {
                "probability": 1,
                "resistances": [ACID],
                "magic": [["PLACEHOLDER"]]
            },
            GOLD_DRAGONBORN: {
                "probability": 1,
                "resistances": [FIRE],
                "magic": [["PLACEHOLDER"]]
            },
            BRASS_DRAGONBORN: {
                "probability": 1,
                "resistances": [FIRE],
                "magic": [["PLACEHOLDER"]]
            },
            COPPER_DRAGONBORN: {
                "probability": 1,
                "resistances": [ACID],
                "magic": [["PLACEHOLDER"]]
            },
            RED_DRAGONBORN: {
                "probability": 1,
                "resistances": [FIRE],
                "magic": [["PLACEHOLDER"]]
            },
            GREEN_DRAGONBORN: {
                "probability": 1,
                "resistances": [POISON],
                "magic": [["PLACEHOLDER"]]
            }
        }
    },

    ELF: {
        "probability": 10,
        "development age": 18,
        "size": MEDIUM,
        "type:": HUMANOID,
        "health base": 4,
        "age max": 750,
        "languages": [COMMON_LANGUAGE, ELVISH_LANGUAGE, RANDOM_STANDARD],
        "male names": ["Adran", "Aelar", "Aramil", "Arannis", "Aust", "Azaki", "Beiro", "Berrian", "Caeldrim", "Carric", "Dayereth", "Dreali", "Efferil", "Eiravel", "Enialis", "Erdan", "Erevan", "Fivin", "Galinndan", "Gennal", "Hadarai", "Halimath", "Heian", "Himo", "Immeral", "Ivellios", "Korfel", "Lamlis", "Laucian", "Lucan", "Mindartis", "Naal", "Nutae", "Paelias", "Peren", "Quarion", "Riardon", "Rolen", "Soveliss", "Suhnae", "Thamior", "Tharivol", "Theren", "Theriatis", "Thervan", "Uthemar", "Vanuath", "Varis"],
        "female names": ["Adrie", "Ahinar", "Althaea", "Anastrianna", "Andraste", "Antinua", "Arara", "Baelitae", "Bethrynna", "Birel", "Caelynn", "Chaedi", "Claira", "Dara", "Drusilia", "Elama", "Enna", "Faral", "Felosial", "Hatae", "Ielenia", "Ilanis", "Irann", "Jarsali", "Jelenneth", "Keyleth", "Leshanna", "Lia", "Maiathah", "Malquis", "Meriele", "Mialee", "Myathethil", "Naivara", "Quelenna", "Quillathe", "Ridaro", "Sariel", "Shanairla", "Shava", "Silaqui", "Sumnes", "Theirastra", "Thiala", "Tiaathque", "Traulam", "Vadania", "Valanthe", "Valna", "Xanaphia"],
        "last names": ["Aloro", "Amakiir", "Amastacia", "Ariessus", "Arnuanna", "Berevan", "Caerdonel", "Caphaxath", "Casilltenirra", "Cithreth", "Dalanthan", "Eathalena", "Erenaeth", "Ethanasath", "Fasharash", "Firahel", "Floshem", "Galanodel", "Goltorah", "Hanali", "Holimion", "Horineth", "Iathrana", "Ilphelkiir", "Iranapha", "Koehlanna", "Lathalas", "Liadon", "Meliamne", "Mellerelel", "Mystralath", "Naïlo", "Netyoive", "Ofandrus", "Ostoroth", "Othronus", "Qualanthri", "Raethran", "Rothenel", "Selevarun", "Siannodel", "Suithrasas", "Sylvaranth", "Teinithra", "Tiltathana", "Wasanthi", "Withrethin", "Xiloscient", "Xistsrith", "Yaeldrin"],
        
        "speed": 30,
        "darkvision": 60,
        "skill proficiencies": [[PERCEPTION, INSIGHT, SURVIVAL]],
        "condition advantages": [CHARMED],
        "condition immunities": [MAGIC_SLEEP],
        "misc": MISC_ELF,
        "lineages": {
            RANDOM: {
                
            },
            HIGH_ELF: {
                "probability": 1,
                "probability alignment 1": [90, 7, 3],
                "probability alignment 2": [3, 7, 90],
                "base height": 4 + 6/12,
                "height modifier": "2d10",
                "base weight": 90,
                "weight modifier": "1d4",
                
                "misc": MISC_HIGH_ELF,
                "magic": [["PLACEHOLDER"]]
            },
            WOOD_ELF: {
                "probability": 1,
                "probability alignment 1": [90, 7, 3],
                "probability alignment 2": [3, 7, 90],
                "base height": 4 + 6/12,
                "height modifier": "2d10",
                "base weight": 100,
                "weight modifier": "1d4",
                
                "speed": 5,
                "magic": [["PLACEHOLDER"]]
            },
            DROW: {
                "probability": 1,
                "probability alignment 1": [90, 7, 3],
                "probability alignment 2": [90, 7, 3],
                "base height": 4 + 5/12,
                "height modifier": "2d6",
                "base weight": 100,
                "weight modifier": "1d6",

                "darkvision": 60,
                "magic": [["PLACEHOLDER"]],
            }
        }
    },

    GNOME: {
        "probability": 1,
        "probability alignment 1": [4, 2, 4],
        "probability alignment 2": [3, 7, 90],
        "development age": 20,
        "size": SMALL,
        "type:": HUMANOID,
        "age max": 425,
        "base height": 2 + 11/12,
        "height modifier": "2d4",
        "base weight": 35,
        "weight modifier": "1",
        "health base": 4,
        "languages": [COMMON_LANGUAGE, GNOMISH_LANGUAGE, RANDOM_STANDARD],
        "male names": ["Alston", "Alvyn", "Anverth", "Arumawann", "Bilbron", "Boddynock", "Brocc", "Burgell", "Cockaby", "Crampernap", "Dabbledob", "Delebean", "Dimble", "Eberdeb", "Eldon", "Erky", "Fablen", "Fibblestib", "Fonkin", "Frouse", "Frug", "Gerbo", "Gimble", "Glim", "Igden", "Jabble", "Jebeddo", "Kellen", "Kipper", "Namfoodle", "Oppleby", "Orryn", "Paggen", "Pallabar", "Pog", "Qualen", "Ribbles", "Rimple", "Roondar", "Sapply", "Seebo", "Senteq", "Sindri", "Umpen", "Warryn", "Wiggens", "Wobbles", "Wrenn", "Zaffrab", "Zook"],
        "female names": ["Abalaba", "Bimpnottin", "Breena", "Buvvie", "Callybon", "Caramip", "Carlin", "Cumpen", "Dalaba", "Donella", "Duvamil", "Ella", "Ellyjoybell", "Ellywick", "Enidda", "Lilli", "Loopmottin", "Lorilla", "Luthra", "Mardnab", "Meena", "Menny", "Mumpena", "Nissa", "Numba", "Nyx", "Oda", "Oppah", "Orla", "Panana", "Pyntle", "Quilla", "Ranala", "Reddlepop", "Roywyn", "Salanop", "Shamil", "Siffress", "Symma", "Tana", "Tenena", "Tervaround", "Tippletoe", "Ulla", "Unvera", "Velptima", "Virra", "Waywocket", "Yebe", "Zanna"],
        "last names": ["Albaratie", "Bafflestone", "Beren", "Boondiggles", "Cobblelob", "Daergel", "Dunben", "Fabblestabble", "Fapplestamp", "Fiddlefen", "Folkor", "Garrick", "Gimlen", "Glittergem", "Gobblefirn", "Gummen", "Horcusporcus", "Humplebumple", "Ironhide", "Leffery", "Lingenhall", "Loofollue", "Maekkelferce", "Miggledy", "Munggen", "Murnig", "Musgraben", "Nackle", "Ningel", "Nopenstallen", "Nucklestamp", "Offund", "Oomtrowl", "Pilwicken", "Pingun", "Quillsharpener", "Raulnor", "Reese", "Rofferton", "Scheppen", "Shadowcloak", "Silverthread", "Sympony", "Tarkelby", "Timbers", "Turen", "Umbodoben", "Waggletop", "Welber", "Wildwander"],
        
        "speed": 30,
        "darkvision": 60,
        "save advantages": [INTELLIGENCE, WISDOM, CHARISMA],
        "misc": MISC_GNOME,
        "lineages": {
            RANDOM: {
                
            },
            FOREST_GNOME: {
                "probability": 1,
                "magic": [["PLACEHOLDER"]]
            },
            ROCK_GNOME: {
                "probability": 1,
                "misc": MISC_ROCK_GNOME
            }
        }
    },

    GOLIATH: {
        "probability": 1,
        "size": MEDIUM,
        "type:": HUMANOID,
        "probability alignment 1": [3, 7, 90],
        "probability alignment 2": [90, 7, 3],
        "base height": 6 + 2/12,
        "height modifier": "2d10",
        "base weight": 110,
        "weight modifier": "2d4",
        "languages": [COMMON_LANGUAGE, GIANT_LANGUAGE, RANDOM_STANDARD],
        "male names": ["Aukan", "Eglath", "Gae-Al", "Gauthak", "Ilikan", "Keothi", "Kuori", "Lo-Kag", "Manneo", "Maveith", "Nalla", "Orilo", "Paavu", "Pethani", "Thalai", "Thotham", "Uthal", "Vaunea", "Vimak"],
        "female names": ["Aukan", "Eglath", "Gae-Al", "Gauthak", "Ilikan", "Keothi", "Kuori", "Lo-Kag", "Manneo", "Maveith", "Nalla", "Orilo", "Paavu", "Pethani", "Thalai", "Thotham", "Uthal", "Vaunea", "Vimak"],
        "last names": ["Anakalathai", "Elanithino", "Gathakanathi", "Kalagiano", "Katho-Olavi", "Kolae-Gileana", "Ogolakanu", "Thuliaga", "Thunukalathi", "Vaimei-Laga"],
        
        "condition advantages": [GRAPPLED],
        "misc": MISC_GOLIATH,
        "speed": 35,
        "lineages": {
            RANDOM: {
                
            },
            CLOUD_GIANT: {
                "probability": 1,
                "misc": MISC_CLOUD_GIANT
            },
            FIRE_GIANT: {
                "probability": 1,
                "misc": MISC_FIRE_GIANT
            },
            FROST_GIANT: {
                "probability": 1,
                "misc": MISC_FROST_GIANT
            },
            HILL_GIANT: {
                "probability": 1,
                "misc": MISC_HILL_GIANT
            },
            STONE_GIANT: {
                "probability": 1,
                "misc": MISC_STONE_GIANT
            },
            STORM_GIANT: {
                "probability": 1,
                "misc": MISC_STORM_GIANT
            }
        }
    },

    HALFLING: {
        "probability": 10,
        "probability alignment 1": [3, 7, 90],
        "probability alignment 2": [3, 7, 90],
        "development age": 20,
        "size": SMALL,
        "type:": HUMANOID,
        "languages": [COMMON_LANGUAGE, HALFLING_LANGUAGE, RANDOM_STANDARD],
        "health base": 4,
        "age max": 150,
        "base height": 2 + 7/12,
        "height modifier": "2d8",
        "base weight": 35,
        "weight modifier": "1",
        "male names": ["Alton", "Ander", "Bernie", "Bobbin", "Cade", "Callus", "Corrin", "Dannad", "Danniel", "Eddie", "Egart", "Eldon", "Errich", "Fildo", "Finnan", "Franklin", "Garret", "Garth", "Gilbert", "Gob", "Harol", "Igor", "Jasper", "Keith", "Kevin", "Lazam", "Lerry", "Lindal", "Lyle", "Merric", "Mican", "Milo", "Morrin", "Nebin", "Nevil", "Osborn", "Ostran", "Oswalt", "Perrin", "Poppy", "Reed", "Roscoe", "Sam", "Shardon", "Tye", "Ulmo", "Wellby", "Wendel", "Wenner", "Wes"],
        "female names": ["Alain", "Andry", "Anne", "Bella", "Blossom", "Bree", "Callie", "Chenna", "Cora", "Dee", "Dell", "Eida", "Eran", "Euphemia", "Georgina", "Gynnie", "Harriet", "Jasmine", "Jillian", "Jo", "Kithri", "Lavinia", "Lidda", "Maegan", "Marigold", "Merla", "Myria", "Nedda", "Nikki", "Nora", "Olivia", "Paela", "Pearl", "Pennie", "Philomena", "Portia", "Robbie", "Rose", "Saral", "Seraphina", "Shaena", "Stacee", "Tawna", "Thea", "Trym", "Tyna", "Vani", "Verna", "Wella", "Willow"],
        "last names": ["Appleblossom", "Bigheart", "Brightmoon", "Brushgather", "Cherrycheeks", "Copperkettle", "Deephollow", "Elderberry", "Fastfoot", "Fatrabbit", "Glenfellow", "Goldfound", "Tallbarrel", "Tallearth", "Greenbottle", "Greenleaf", "High-hill", "Hilltopple", "Hogcollar", "Honeypot", "Jamjar", "Kettlewhistle", "Leagallow", "Littlefoot", "Nimblefingers", "Porridgepot", "Quickstep", "Reedfellow", "Shadowquick", "Silvereyes", "Smoothhands", "Stonebridge", "Stoutbridge", "Stoutman", "Strongbones", "Sunmeadow", "Swiftwhistle", "Tallfellow", "Tealeaf", "Tenpenny", "Thistletop", "Thorngage", "Tosscobble", "Underbough", "Underfoot", "Warmwater", "Whispermouse", "Wildcloak", "Wildheart", "Wiseacre"],
        
        "speed": 30,
        "darkvision": 0,
        "condition advantages": [FRIGHTENED],
        "misc": MISC_HALFLING,
        "lineages": {
            RANDOM: {
                
            },
            LIGHTFOOT_HALFLING: {
                "probability": 1
            },
            STOUT_HALFLING: {
                "probability": 1
            }
        }
    },

    ORC: {
        "probability": 1,
        "probability alignment 1": [90, 7, 3],
        "probability alignment 2": [90, 7, 3],
        "size": MEDIUM,
        "type:": HUMANOID,
        "languages": [COMMON_LANGUAGE, ORC_LANGUAGE, RANDOM_STANDARD],
        "health base": 4,
        "base height": 5 + 4/12,
        "height modifier": "2d8",
        "base weight": 175,
        "weight modifier": "2d6",
        "male names": ["Argran", "Braak", "Brug", "Cagak", "Dench", "Dorn", "Dren", "Druuk", "Feng", "Gell", "Gnarsh", "Grumbar", "Gubrash", "Hagren", "Henk", "Hogar", "Holg", "Imsh", "Karash", "Karg", "Keth", "Korag", "Krusk", "Lubash", "Megged", "Mhurren", "Mord", "Morg", "Nil", "Nybarg", "Odorr", "Ohr", "Rendar", "Resh", "Ront", "Rrath", "Sark", "Scrag", "Sheggen", "Shump", "Tanglar", "Tarak", "Thar", "Thokk", "Trag", "Ugarth", "Varg", "Vilberg", "Yurk", "Zed"],
        "female names": ["Arha", "Baggi", "Bendoo", "Bilga", "Brakka", "Creega", "Drenna", "Ekk", "Emen", "Engong", "Fistula", "Gaaki", "Gorga", "Grai", "Greeba", "Grigi", "Gynk", "Hrathy", "Huru", "Ilga", "Kabbarg", "Kansif", "Lagazi", "Lezre", "Murgen", "Murook", "Myev", "Nagrette", "Neega", "Nella", "Nogu", "Oolah", "Ootah", "Ovak", "Ownka", "Puyet", "Reeza", "Shautha", "Silgre", "Sutha", "Tagga", "Tawar", "Tomph", "Ubada", "Vanchu", "Vola", "Volen", "Vorka", "Yevelda", "Zagga"],
        "last names": [],
       
        "speed": 30,
        "darkvision": 120,
        "misc": MISC_ORC,
         "lineages": {
            NONE_M: {
                "probability": 1
            }
        }
    },


    DWARF: {
        "probability": 10,
        "probability alignment 1": [3, 7, 90],
        "probability alignment 2": [3, 7, 90],
        "development age": 20,
        "size": MEDIUM,
        "type": HUMANOID,
        "languages": [COMMON_LANGUAGE, DWARVISH_LANGUAGE, RANDOM_STANDARD],
        "health base": 4,
        "male names": ["Adrik", "Alberich", "Baern", "Barendd", "Beloril", "Brottor", "Dain", "Dalgal", "Darrak", "Delg", "Duergath", "Dworic", "Eberk", "Einkil", "Elaim", "Erias", "Fallond", "Fargrim", "Gardain", "Gilthur", "Gimgen", "Gimurt", "Harbek", "Kildrak", "Kilvar", "Morgran", "Morkral", "Nalral", "Nordak", "Nuraval", "Oloric", "Olunt", "Orsik", "Oskar", "Rangrim", "Reirak", "Rurik", "Taklinn", "Thoradin", "Thorin", "Thradal", "Tordek", "Traubon", "Travok", "Ulfgar", "Uraim", "Veit", "Vonbin", "Vondal", "Whurbin"],
        "female names": ["Anbera", "Artin", "Audhild", "Balifra", "Barbena", "Bardryn", "Bolhild", "Dagnal", "Dariff", "Delre", "Diesa", "Eldeth", "Eridred", "Falkrunn", "Fallthra", "Finellen", "Gillydd", "Gunnloda", "Gurdis", "Helgret", "Helja", "Hlin", "Ilde", "Jarana", "Kathra", "Kilia", "Kristryd", "Liftrasa", "Marastyr", "Mardred", "Morana", "Nalaed", "Nora", "Nurkara", "Oriff", "Ovina", "Riswynn", "Sannl", "Therlin", "Thodris", "Torbera", "Tordrid", "Torgga", "Urshar", "Valida", "Vistra", "Vonana", "Werydd", "Whurdred", "Yurgunn"],
        "last names": ["Aranore", "Balderk", "Battlehammer", "Bigtoe", "Bloodkith", "Bofdann", "Brawnanvil", "Brazzik", "Broodfist", "Burrowfound", "Caebrek", "Daerdahk", "Dankil", "Daraln", "Deepdelver", "Durthane", "Eversharp", "Fallack", "Fireforge", "Foamtankard", "Frostbeard", "Glanhig", "Goblinbane", "Goldfinder", "Gorunn", "Graybeard", "Hammerstone", "Helcral", "Holderhek", "Ironfist", "Loderr", "Lutgehr", "Morigak", "Orcfoe", "Rakankrak", "Ruby-Eye", "Rumnaheim", "Silveraxe", "Silverstone", "Steelfist", "Stoutale", "Strakeln", "Strongheart", "Thrahak", "Torevir", "Torunn", "Trollbleeder", "Trueanvil", "Trueblood", "Ungart"],
        "age max": 350,

        "speed": 30,
        "darkvision": 120,
        "condition advantages": [POISONING],
        "resistances": [POISON],
        "health bonus": 1,
        "misc": MISC_DWARF,
        "lineages": {
            RANDOM: {
                
            },
            MOUNTAIN_DWARF: {
                "probability": 1,
                "base height": 4,
                "height modifier": "2d4",
                "base weight": 130,
                "weight modifier": "2d6",
            },
            HILL_DWARF: {
                "probability": 1,
                "base height": 3 + 8/12,
                "height modifier": "2d4",
                "base weight": 115,
                "weight modifier": "2d6"
            }
        }
    },

    TIEFLING: {
        "probability": 1,
        "probability alignment 1": [90, 7, 3],
        "probability alignment 2": [5, 3, 2],
        "development age": 18,
        "size": MEDIUM,
        "type:": HUMANOID,
        "languages": [COMMON_LANGUAGE, INFERNAL_LANGUAGE, RANDOM_STANDARD],
        "health base": 4,
        "age max": 100,
        "base height": 4 + 9/12,
        "height modifier": "2d8",
        "base weight": 110,
        "weight modifier": "2d4",
        "male names": ["Abad", "Ahrim", "Akmen", "Amnon", "Andram", "Astar", "Balam", "Barakas", "Bathin", "Caim", "Chem", "Cimer", "Cressel", "Damakos", "Ekemon", "Euron", "Fenriz", "Forcas", "Habor", "Iados", "Kairon", "Leucis", "Mamnen", "Mantus", "Marbas", "Melech", "Merihim", "Modean", "Mordai", "Mormo", "Morthos", "Nicor", "Nirgel", "Oriax", "Paymon", "Pelaios", "Purson", "Qemuel", "Raam", "Rimmon", "Sammal", "Skamos", "Tethren", "Thamuz", "Therai", "Valafar", "Vassago", "Xappan", "Zepar", "Zephan"],
        "female names": ["Akta", "Anakis", "Armara", "Astaro", "Aym", "Azza", "Beleth", "Bryseis", "Bune", "Criella", "Damaia", "Decarabia", "Ea", "Gadreel", "Gomory", "Hecat", "Ishte", "Jezebeth", "Kali", "Kallista", "Kasdeya", "Lerissa", "Lilith", "Makaria", "Manea", "Markosian", "Mastema", "Naamah", "Nemeia", "Nija", "Orianna", "Osah", "Phelaia", "Prosperine", "Purah", "Pyra", "Rieta", "Ronobe", "Ronwe", "Seddit", "Seere", "Sekhmet", "Semyaza", "Shava", "Shax", "Sorath", "Uzza", "Vapula", "Vepar", "Verin"],
        "last names": TIEFLING_VALUES,
        
        "speed": 30,
        "darkvision": 60,
        "magic": [["PLACEHOLDER"]],
        "lineages": {
            RANDOM: {

            },
            ABYSSAL_TIEFLING: {
                "probability": 1,
                "resistances": [POISON],
                "magic": [["PLACEHOLDER"]]
            },
            CHTONIC_TIEFLING: {
                "probability": 1,
                "resistances": [NECROTIC],
                "magic": [["PLACEHOLDER"]]
            },
            INFERNAL_TIEFLING: {
                "probability": 1,
                "resistances": [FIRE],
                "magic": [["PLACEHOLDER"]]
            }
        }
    },

    HUMAN: {
        "probability": 20,
        "size": MEDIUM,
        "languages": [COMMON_LANGUAGE, RANDOM_STANDARD, RANDOM_STANDARD],
        "age max": 90,
        "probability alignment 1": [1, 1, 1],
        "probability alignment 2": [1, 1, 1],
        "development age": 18,
        "health base": 4,
        "base height": 4 + 8/12,
        "height modifier": "2d10",
        "base weight": 110,
        "weight modifier": "2d4",

        "skill proficiencies": [list(SKILLS.keys())],
        "feat": ORIGIN_FEATS,
        "speed": 30,
        "darkvision": 0,
        "misc": MISC_HUMAN,
        "lineages": {
            RANDOM: {
                
            },
            CALISHITA: {
                "probability": 1,
                "male names": ["Aseir", "Bardeid", "Haseid", "Khemed", "Mehmen", "Sudeiman", "Zasheir"],
                "female names": ["Atala", "Ceidil", "Hama", "Jasmal", "Meilil", "Seipora", "Yasheira", "Zasheida"],
                "last names": ["Basha", "Dumein", "Jassan", "Khalid", "Mostana", "Pashar", "Rein"]
            },
            CHONDATHAN: {
                "probability": 1,
                "male names": ["Darvin", "Dorn", "Evendur", "Gorstag", "Grim", "Hel", "Malark", "Morn", "Randal", "Stedd"],
                "female names": ["Arveene", "Esvele", "Jhessail", "Kerri", "Lureene", "Miri", "Rowan", "Shandri", "Tessele"],
                "last names": ["Amblecrown", "Buckman", "Dundragon", "Evenwood", "Greycastle", "Tallstag"]
            },
            DAMARAN: {
                "probability": 1,
                "male names": ["Bor", "Fodel", "Glar", "Grigor", "Igan", "Ivor", "Kosef", "Mival", "Orel", "Pavel", "Sergor"],
                "female names": ["Alethra", "Kara", "Katernin", "Mara", "Natali", "Olma", "Tana", "Zora"],
                "last names": ["Bersk", "Chernin", "Dotsk", "Kulenov", "Marsk", "Nemetsk", "Shemov", "Starag"]
            },
            ILLUSKAN: {
                "probability": 1,
                "male names": ["Ander", "Blath", "Bran", "Frath", "Geth", "Lander", "Luth", "Malcer", "Stor", "Taman", "Urth"],
                "female names": ["Amafrey", "Betha", "Cefrey", "Kethra", "Mara", "Olga", "Silifrey", "Westra"],
                "last names": ["Brightwood", "Helder", "Hornraven", "Lackmen", "Stormwind", "Windrivver"]
            },
            MULAN: {
                "probability": 1,
                "male names": ["Aoth", "Bareris", "Ehput-Ki", "Kethoth", "Mumed", "Ramas", "So-Kehur", "Thazar-De", "Urhur"],
                "female names": ["Arizima", "Charhi", "Nephis", "Nulara", "Murithi", "Sefris", "Thola", "Umara", "Zolis"],
                "last names": ["Ankhalab", "Anskuld", "Fezim", "Hahpet", "Nathandem", "Sepret", "Uuthrakt"]
            },
            RASHEMI: {
                "probability": 1,
                "male names": ["Borivik", "Faurgar", "Jandar", "Kanithar", "Madislak", "Ralmevik", "Shaumar", "Vladislak"],
                "female names": ["Fyevarra", "Hulmarra", "Immith", "Imzel", "Navarra", "Shevarra", "Tammith", "Yuldra"],
                "last names": ["Chergoba", "Dyernina", "Iltazyara", "Murneyethara", "Stayanoga", "Ulmokina"]
            },
            SHOU: {
                "probability": 1,
                "male names": ["An", "Chen", "Chi", "Fai", "Jiang", "Jun", "Lian", "Long", "Meng", "On", "Shan", "Shui", "Wen"],
                "female names": ["Bai", "Chao", "Jia", "Lei", "Mei", "Qiao", "Shui", "Tai"],
                "last names": ["Chien", "Huang", "Kao", "Kung", "Lao", "Ling", "Mei", "Pin", "Shin", "Sum", "Tan", "Wan"]
            },
            TETHYRIAN: {
                "probability": 1,
                "male names": ["Darvin", "Dorn", "Evendur", "Gorstag", "Grim", "Hel", "Malark", "Morn", "Randal", "Stedd"],
                "female names": ["Arveene", "Esvele", "Jhessail", "Kerri", "Lureene", "Miri", "Rowan", "Shandri", "Tessele"],
                "last names": ["Amblecrown", "Buckman", "Dundragon", "Evenwood", "Greycastle", "Tallstag"]
            },
            TURAMI: {
                "probability": 1,
                "male names": ["Anton", "Diero", "Marcon", "Pieron", "Rimardo", "Romero", "Salazar", "Umbero"],
                "female names": ["Balama", "Dona", "Faila", "Jalana", "Luisa", "Marta", "Quara", "Selise", "Vonda"],
                "last names": ["Agosto", "Astorio", "Calabra", "Domine", "Falone", "Marivaldi", "Pisacar", "Ramondo"]
            }
        }
    }
}
SPECIES[AASIMAR]["lineages"] = SPECIES[HUMAN]["lineages"]
SPECIES[AASIMAR]["size"] = SPECIES[HUMAN]["size"]
SPECIES[AASIMAR]["probability alignment 1"] = SPECIES[HUMAN]["probability alignment 1"]
SPECIES[AASIMAR]["probability alignment 2"] = SPECIES[HUMAN]["probability alignment 2"]
SPECIES[AASIMAR]["development age"] = SPECIES[HUMAN]["development age"]
SPECIES[AASIMAR]["health base"] = SPECIES[HUMAN]["health base"]
SPECIES[AASIMAR]["base height"] = SPECIES[HUMAN]["base height"]
SPECIES[AASIMAR]["height modifier"] = SPECIES[HUMAN]["height modifier"]
SPECIES[AASIMAR]["base weight"] = SPECIES[HUMAN]["base weight"]
SPECIES[AASIMAR]["weight modifier"] = SPECIES[HUMAN]["weight modifier"]

SPECIES_WEIGHTS = {species: info["probability"] for species, info in SPECIES.items() if "probability" in info}

LINEAGE_WEIGHTS = {species: {lineage: info["probability"] for lineage, info in SPECIES[species]["lineages"].items() if "probability" in info} for species, _ in SPECIES.items()}

# How probable it is a specific species/lineage will get an alignment. I arbitrarily set the weights
# 1 stands for lawful/chaotic, 2 stands for good/evil. A stands for alignments based on species, B stands for alignments based on lineages
# This is a bit of an overly convoluted calculation, but it allows for a lot of flexibility in the future
ALIGNMENT_WEIGHTS_1A = {species: info["probability alignment 1"] for species, info in SPECIES.items() if "probability alignment 1" in info}

ALIGNMENT_WEIGHTS_1B = {}
for species in SPECIES.values():
    for lineage, lineage_info in species["lineages"].items():
        if "probability alignment 1" in lineage_info:
            ALIGNMENT_WEIGHTS_1B[lineage] = lineage_info["probability alignment 1"]


ALIGNMENT_WEIGHTS_1 = {**ALIGNMENT_WEIGHTS_1A, **ALIGNMENT_WEIGHTS_1B}

ALIGNMENT_WEIGHTS_2A = {species: info["probability alignment 2"] for species, info in SPECIES.items() if "probability alignment 2" in info}

ALIGNMENT_WEIGHTS_2B = {}
for species in SPECIES.values():
    for lineage, lineage_info in species["lineages"].items():
       if "probability alignment 2" in lineage_info:
             ALIGNMENT_WEIGHTS_2B[lineage] = lineage_info["probability alignment 2"]

ALIGNMENT_WEIGHTS_2 = {**ALIGNMENT_WEIGHTS_2A, **ALIGNMENT_WEIGHTS_2B}

BACKGROUNDS: Dict[str, Dict] = {
    RANDOM: {

    },
    ACOLYTE: {
        "probability": 1,
        "abilities bonus": [INTELLIGENCE, WISDOM, CHARISMA],
        "feat": MAGIC_INITIATE, # UPDATE: Cleric
        "skill proficiencies": [INSIGHT, RELIGION],
        "tool proficiencies": [CALLIGRAPHERS_SUPPLIES],
        "equipment": [[RANDOM, ACOLYTE_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [CALLIGRAPHERS_SUPPLIES, BOOK_PRAYERS, HOLY_SYMBOL, f"10MULTIPLE {PARCHMENT_SHEETS}", ROBE, f"8 {GOLD}"]
    },
    ARTISAN: {
        "probability": 1,
        "abilities bonus": [STRENGTH, DEXTERITY, INTELLIGENCE],
        "feat": CRAFTER,
        "skill proficiencies": [INVESTIGATION, PERSUASION],
        "tool proficiencies": [[ARTISANS_TOOLS, True]],
        "equipment": [[RANDOM, ARTISAN_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [f"2MULTIPLE {POUCH}", TRAVELERS_CLOTHES, f"32 {GOLD}"]
    },
    CHARLATAN: {
        "probability": 1,
        "abilities bonus": [DEXTERITY, CONSTITUTION, CHARISMA],
        "feat": SKILLED,
        "skill proficiencies": [DECEPTION, SLEIGHT_OF_HAND],
        "tool proficiencies": [FORGERY_KIT],
        "equipment": [[RANDOM, CHARLATAN_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [FORGERY_KIT, COSTUME, FINE_CLOTHES, f"15 {GOLD}"]
    },
    CRIMINAL: {
        "probability": 1,
        "abilities bonus": [DEXTERITY, CONSTITUTION, INTELLIGENCE],
        "feat": ALERT,
        "skill proficiencies": [SLEIGHT_OF_HAND, STEALTH],
        "tool proficiencies": [THIEVES_TOOLS],
        "equipment": [[RANDOM, CRIMINAL_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [THIEVES_TOOLS, f"2MULTIPLE {DAGGER}", CROWBAR, f"2MULTIPLE {POUCH}", TRAVELERS_CLOTHES, f"16 {GOLD}"]
    },
    ENTERTAINER: {
        "probability": 1,
        "abilities bonus": [STRENGTH, DEXTERITY, CHARISMA],
        "feat": MUSICIAN,
        "skill proficiencies": [ACROBATICS, PERFORMANCE],
        "tool proficiencies": [[MUSICAL_INSTRUMENTS, True]],
        "equipment": [[RANDOM, ENTERTAINER_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [f"2MULTIPLE {COSTUME}", MIRROR, PERFUME, TRAVELERS_CLOTHES, f"11 {GOLD}"]
    },
    FARMER: {
        "probability": 1,
        "abilities bonus": [STRENGTH, CONSTITUTION, WISDOM],
        "feat": TOUGH,
        "skill proficiencies": [ANIMAL_HANDLING, NATURE],
        "tool proficiencies": [CARPENTERS_TOOLS],
        "equipment": [[RANDOM, FARMER_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [SICKLE, CARPENTERS_TOOLS, HEALERS_KIT, IRON_POT, SHOVEL, TRAVELERS_CLOTHES, f"30 {GOLD}"]
    },
    GUARD: {
        "probability": 1,
        "abilities bonus": [STRENGTH, INTELLIGENCE, WISDOM],
        "feat": ALERT,
        "skill proficiencies": [ATHLETICS, PERCEPTION],
        "tool proficiencies": [[GAMING_SETS, True]],
        "equipment": [[RANDOM, GUARD_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [SPEAR, CROSSBOW_LIGHT, f"20MULTIPLE {BOLT}", HOODED_LANTERN, MANACLES, QUIVER, TRAVELERS_CLOTHES, f"12 {GOLD}"]
    },
    GUIDE: {
        "probability" : 1,
        "abilities bonus": [DEXTERITY, CONSTITUTION, WISDOM],
        "feat": MAGIC_INITIATE, # UPDATE: Druid
        "skill proficiencies": [STEALTH, SURVIVAL],
        "tool proficiencies": [CARTOGRAPHERS_TOOLS],
        "equipment": [[RANDOM, GUIDE_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [SHORTBOW, f"20MULTIPLE {ARROW}", CARTOGRAPHERS_TOOLS, BEDROLL, QUIVER, TENT, TRAVELERS_CLOTHES, f"3 {GOLD}"]
    },
    HERMIT: {
        "probability": 1,
        "abilities bonus": [CONSTITUTION, WISDOM, CHARISMA],
        "feat": HEALER,
        "skill proficiencies": [MEDICINE, RELIGION],
        "tool proficiencies": [HERBALISM_KIT],
        "equipment": [[RANDOM, HERMIT_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [HERBALISM_KIT, BEDROLL, QUARTERSTAFF, BOOK_PHILOSPHY, LAMP, f"3MULTIPLE {OIL_FLASK}", TRAVELERS_CLOTHES, f"16 {GOLD}"]
    },
    MERCHANT: {
        "probability": 1,
        "abilities bonus": [CONSTITUTION, INTELLIGENCE, CHARISMA],
        "feat": LUCKY,
        "skill proficiencies": [ANIMAL_HANDLING, PERSUASION],
        "tool proficiencies": [NAVIGATORS_TOOLS],
        "equipment": [[RANDOM, MERCHANT_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [NAVIGATORS_TOOLS, f"2MULTIPLE {POUCH}", TRAVELERS_CLOTHES, f"2 {GOLD}"]
    },
    NOBLE: {
        "probability": 1,
        "abilities bonus": [STRENGTH, INTELLIGENCE, CHARISMA],
        "feat": SKILLED,
        "skill proficiencies": [HISTORY, PERSUASION],
        "tool proficiencies": [[GAMING_SETS, True]],
        "equipment": [[RANDOM, NOBLE_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [FINE_CLOTHES, PERFUME, f"29 {GOLD}"]
    },
    SAGE: {
        "probability": 1,
        "abilities bonus": [CONSTITUTION, INTELLIGENCE, WISDOM],
        "feat": MAGIC_INITIATE, # UPDATE: Wizard
        "skill proficiencies": [ARCANA, HISTORY],
        "tool proficiencies": [CALLIGRAPHERS_SUPPLIES],
        "equipment": [[RANDOM, SAGE_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [QUARTERSTAFF, CALLIGRAPHERS_SUPPLIES, BOOK_HISTORY, f"8MULTIPLE {PARCHMENT_SHEETS}", ROBE, f"8 {GOLD}"]
    },
    SAILOR: {
        "probability": 1,
        "abilities bonus": [STRENGTH, DEXTERITY, WISDOM],
        "feat": TAVERN_BRAWLER,
        "skill proficiencies": [ACROBATICS, PERCEPTION],
        "tool proficiencies": [NAVIGATORS_TOOLS],
        "equipment": [[RANDOM, SAILOR_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [DAGGER, NAVIGATORS_TOOLS, ROPE, TRAVELERS_CLOTHES, f"20 {GOLD}"]
    },
    SCRIBE: {
        "probability": 1,
        "abilities bonus": [DEXTERITY, INTELLIGENCE, WISDOM],
        "feat": SKILLED,
        "skill proficiencies": [INVESTIGATION, PERCEPTION],
        "tool proficiencies": [CALLIGRAPHERS_SUPPLIES],
        "equipment": [[RANDOM, SCRIBE_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [CALLIGRAPHERS_SUPPLIES, FINE_CLOTHES, f"12MULTIPLE {PARCHMENT_SHEETS}", LAMP, f"3MULTIPLE {OIL_FLASK}", f"23 {GOLD}"]
    },
    SOLDIER: {
        "probability": 1,
        "abilities bonus": [STRENGTH, DEXTERITY, CONSTITUTION],
        "feat": SAVAGE_ATTACKER,
        "skill proficiencies": [ATHLETICS, INTIMIDATION],
        "tool proficiencies": [GAMING_SETS],
        "equipment": [[RANDOM, SOLDIER_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [SPEAR, SHORTBOW, f"20MULTIPLE {ARROW}", HEALERS_KIT, QUIVER, TRAVELERS_CLOTHES, f"14 {GOLD}"]
    },
    WAYFARER: {
        "probability": 1,
        "abilities bonus": [DEXTERITY, WISDOM, CHARISMA],
        "feat": LUCKY,
        "skill proficiencies": [INSIGHT, STEALTH],
        "tool proficiencies": [THIEVES_TOOLS],
        "equipment": [[RANDOM, WAYFARER_EQUIPMENT, f"50 {GOLD}"]],
        "equip": [THIEVES_TOOLS, f"2MULTIPLE {DAGGER}", GAMING_SETS, BEDROLL, f"2MULTIPLE {POUCH}", TRAVELERS_CLOTHES, f"16 {GOLD}"]
    }
}

BACKGROUND_WEIGHTS = {background: info["probability"] for background, info in BACKGROUNDS.items() if "probability" in info}

SPELLS: Dict[str, Dict[str, int]] = {
    SPELL_LIGHT: {
        "level": 0,
        "lists": [BARD, CLERIC, SORCERER, WIZARD],
        "type": EVOCATION,
        "range": TOUCH,
        "time": ACTION,
        "components": ["V", "M"],
        "materials": [[FIREFLY, PHOSPHORESCENT_MOSS]],
        "duration": "1h"
    }
}

CLASSES: Dict[str, Dict[str, Dict[str, Dict]]] = {
    RANDOM: {
        "subclasses": {
            NONE_F: {
                "probability": 1
            }
        }
    },

    NONE_F: {
        "probability": 99,
        "subclasses": {
            NONE_F: {
                "probability": 1
            }
        }
    },

    BARBARIAN: {
        "probability": 1,
        "base abilities": [15, 13, 14, 8, 12, 10],
        "fixed abilities": [1, 1, 1, 1, 1, 1],
        
        "health dice": 12,
        "save proficiencies": [STRENGTH, CONSTITUTION],
        "skill proficiencies": [[ANIMAL_HANDLING, ATHLETICS, INTIMIDATION, NATURE, PERCEPTION, SURVIVAL], [ANIMAL_HANDLING, ATHLETICS, INTIMIDATION, NATURE, PERCEPTION, SURVIVAL]],
        "weapon proficiencies": [SIMPLE_WEAPONS_TRANSLATE, MARTIAL_WEAPONS_TRANSLATE],
        "armor proficiencies": [LIGHT_ARMORS_TRANSLATE, MEDIUM_ARMORS_TRANSLATE, SHIELDS_TRANSLATE],
        "equipment": [[RANDOM, BARBARIAN_EQUIPMENT, f"75 {GOLD}"]],
        "equip": [PLATE_ARMOR, GREATAXE, EXPLORERS_PACK, f"4MULTIPLE {HANDAXE}", f"15 {GOLD}"],
        "misc": MISC_BARBARIAN,
        "features": [WEAPON_MASTERY_TRANSLATE, RAGE],
        "attributes": ["Unarmored Defense"],
        "masteries": [2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4],
        "subclasses": {
            RANDOM: {
                
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    BARD: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    CLERIC: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    DRUID: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    FIGHTER: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    ROGUE: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    WIZARD: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    MONK: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    PALADIN: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    RANGER: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    SORCERER: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    },

    WARLOCK: {
        "probability": 1,
        "subclasses": {
            RANDOM: {
                    
            },
            "PLACEHOLDER": {
                "probability": 1
            }
        }
    }
}

# Rage bonus for barbarian
RAGE_BONUS = [0, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4]

CLASS_WEIGHTS = {classe: info["probability"] for classe, info in CLASSES.items() if "probability" in info}

SUBCLASS_WEIGHTS = {classe: {subclass: info["probability"] for subclass, info in CLASSES[classe]["subclasses"].items() if "probability" in info} for classe, _ in CLASSES.items()}

TOOLTIPS_WEAPONS = {weapon: f"{WEAPONS[weapon]["damage"]} {WEAPONS[weapon]["type"]} ({WEAPONS[weapon]["mastery"]})" for weapon in WEAPONS}

for weapon in WEAPONS:
    if "damage versatile" in WEAPONS[weapon]:
        TOOLTIPS_WEAPONS[weapon] += f"\n{DAMAGE_VERSATILE_TRANSLATE}: {WEAPONS[weapon]["damage versatile"]}"
    if "range" in WEAPONS[weapon]:
        TOOLTIPS_WEAPONS[weapon] += f"\n{RANGE_TRANSLATE}: {"-".join(str(distance) for distance in WEAPONS[weapon]["range"])}{LENGTH_UNIT}"

TOOLTIPS_SPELLS = {spell: f"{SPELLS[spell]["type"]} ({LEVEL_TRANSLATE} {SPELLS[spell]["level"]})\n{CASTING_TIME}: {SPELLS[spell]["time"]}\n{RANGE}: {SPELLS[spell]["range"]}\n{DURATION}: {SPELLS[spell]["duration"]}\n{COMPONENTS}: " for spell in SPELLS}

# Adding components and materials to the tooltips
for spell in SPELLS:
    for i, component in enumerate(SPELLS[spell]["components"]):
        TOOLTIPS_SPELLS[spell] += component
        if i < len(SPELLS[spell]["components"]) - 1:
            TOOLTIPS_SPELLS[spell] += ", "
    if "materials" in SPELLS[spell]:
        TOOLTIPS_SPELLS[spell] += " ("
        for i, material in enumerate(SPELLS[spell]["materials"]):
            TOOLTIPS_SPELLS[spell] += (f" {OR_TRANSLATE} ").join(material)
            if i < len(SPELLS[spell]["materials"]) - 1:
                TOOLTIPS_SPELLS[spell] += ", "

        TOOLTIPS_SPELLS[spell] += ")"

TOOLTIPS = {**TOOLTIPS_WEAPONS, **TOOLTIPS_SPELLS,
    ALERT: "Competenza in Iniziativa\nScambio di Iniziativa",
    CRAFTER: "Competenza in Strumenti\nSconto\nCostruzione Veloce",
    HEALER: "Medico da Battaglia\nRilanci di Cura",
    LUCKY: "Punti Fortuna\nVantaggio\nSvantaggio",
    MAGIC_INITIATE: "Due Trucchetti\nIncantesimo di Livello 1\nCambio di Incantesimo\n\nRipetibile",
    MUSICIAN: "Competenza in Strumenti\nCanzone Incoraggiante",
    SAVAGE_ATTACKER: "Rilanci di Attacco",
    SKILLED: "Tre Competenze\n\nRipetibile",
    TAVERN_BRAWLER: "Attacco senza Armi Migliorato\nRilanci di Danno\nArma Improvvisata\nSpinta",
    TOUGH: "Punti Ferita Aggiuntivi"
}

TOOLTIPS[SPELL_LIGHT] += "\n\nTocchi un oggetto Grande o più piccolo che non è indossato o trasportato da qualcun altro. Fino alla fine dell'incantesimo, l'oggetto irradia Luce Luminosa in un raggio di 6 metri e Luce Soffusa per ulteriori 6 metri. La luce può essere del colore che desideri.\nCoprire l'oggetto con qualcosa di opaco blocca la luce. L'incantesimo termina se lo lanci nuovamente."

# This is a dictionary that will hold the information about almost everything. Accessible using the key T
DICTIONARY_WEAPONS = {weapon: f"{weapon}<br><br><br>{WEAPONS[weapon]["damage"]} {DAMAGES_TRANSLATE} {WEAPONS[weapon]["type"]}<br><br>{", ".join(WEAPONS[weapon]["properties"])}<br>{", ".join(WEAPONS[weapon]["tags"])}<br><br>" for weapon in WEAPONS}

for weapon in WEAPONS:
    if "cost" in WEAPONS[weapon]:
        DICTIONARY_WEAPONS[weapon] += f"<br>{COST_TRANSLATE}: {WEAPONS[weapon]["cost"]}"
    if "weight" in WEAPONS[weapon]:
        DICTIONARY_WEAPONS[weapon] += f"<br>{WEIGHT_TRANSLATE}: {WEIGHT_MULTIPLIER * int(WEAPONS[weapon]["weight"])}{WEIGHT_UNIT}"
    if "damage versatile" in WEAPONS[weapon]:
        DICTIONARY_WEAPONS[weapon] += f"<br>{DAMAGE_VERSATILE_TRANSLATE}: {WEAPONS[weapon]["damage versatile"]}"
    if "range" in WEAPONS[weapon]:
        DICTIONARY_WEAPONS[weapon] += f"<br>{RANGE_TRANSLATE}: {"-".join(str(distance) for distance in WEAPONS[weapon]["range"])}{LENGTH_UNIT}"

DICTIONARY = {**DICTIONARY_WEAPONS,
    AASIMAR: "Gli aasimar sono creature mortali che covano nel proprio animo una scintilla dei Piani Superiori. Che discendano da una creatura angelica o che siano infusi di potere celestiale, possono alimentare tale scintilla per produrre luce, cura e furia divina.<br>Gli aasimar possono sorgere tra qualunque popolazione di mortali. Somigliano ai propri genitori, ma vivono fino a 160 anni e possiedono caratteristiche che tradiscono il loro retaggio celestiale, come lentiggini metalliche, occhi luminosi, un'aureola o la pelle del colore degli aneli (argento, verde opalescente o rosso bronzeo). Tali caratteristiche sono inizialmente sottili e divengono ovvie quando l'aasimar scopre come rivelare la propria completa natura celestiale.",
    DRAGONBORN: "Gli antenati dei dragonidi nacquero dalle uova di draghi cromatici e metallici. Una storia narra che queste uova fossero benedette dagli dei draconici Bahamut e Tiamat, che desideravano popolare il multiverso con creature create a loro immagine. Un'altra storia sostiene che i draghi abbbiano creato i primi dragonidi senza la benedizione degli dei. Qualunque sia la loro origine, i dragonidi hanno fatto del Piano Materiale la propria casa.<br>I dragonidi hanno l'aspetto di draghi bipedi e senza ali - dotati di scaglie, occhi luminosi e ossa robuste che culminano in corna sulle loro teste - e la loro colorazione ed altre caratteristiche ricordano i loro antenati draconici.",
    DWARF: "I nani sorsero dalla terra in tempi antichi per mano di una divinità della forgia. Chiamato con vari nomi su mondi diversi - Moradin, Reorx e altri - tale dio diede ai nani un'affinità alla pietra ed al metallo ed alla vita sotteranea. Tale dio li rese anche resilienti come montagne, con una durata vitale di circa 350 anni.<br>Tozzi e spesso barbuti, i nani originari scolpirono città e fortezze sui pendii delle montagne ed al di sotto della terra. Le loro più abtucge keggende raccontano di conflitti con i mostri delle cime delle montagne e dell'Underdark, che questi fossero giganti torreggianti o orrori sotterranei. Ispirati da tali racconti, i nani di ogni cultura sovente cantano di valorose imprese - specialmente di piccoli che sconfiggono i possenti.<br>Su alcuni mondi del multiverso, il primo insediamento di nani fu costruito su colline o montagne, ed i loro discendenti prendono rispettivamente il nome di nani delle colline o nani delle montagne. I mondi di Greyhawk e Dragonlance hanno tali comunità.",
    ELF: "Creati dal dio Corellon, i primi elfi potevano cambiare forma a piacimento. Persero questa abilità quando Corellon li maledisse per aver complottato con la divinità Lolth, che tentò senza successo di usurpare il dominio di Corellon. Quando Lolth fu gettata nell'Abisso, la maggior parte degli elfi la rinnegarono e si guadagnarono il perdono di Corellon, ma ciò che il dio aveva preso loro fu perso per sempre.<br>Non più capaci di mutare forma a piacimento, gli elfi si ritirarono nel Feywild, ove la loro sofferenza fu aggravata dall'influenza di tale piano. Col tempo, la curiosità portò molti di loro ad esplorare altri piani di esistenza, inclusi mondi nel Piano Materiale.<br>Gli Elfi hanno orecchie a punta e sono privi di peluria facciale e corporea. Vivono all'incirca 750 anni e quando hanno necessità di riposare non dormono, ma entrano in trance. In tale stato, rimangono coscienti dei propri dintorni, mentre si immergono in ricordi e meditazioni.<br>Ogni ambiente trasforma sottilmente gli elfi che lo abitano per un millennio o più, conferendo loro certi tipi di magia. Drow, elfi superiori ed elfi dei boschi sono esempi di elfi che sono stati così trasformati.",
    DROW: "I drow solitamente abitano nell'Underdark, dal quale sono stati influenzati. Alcuni individui e società drow evitano del tutto l'Underdark, eppure ne conservano la magia. Nell'ambientazione di Eberron, per esempio, i drow abitano in Foreste Pluviali e rovine ciclopice sul continente di Xen'drik.",
    HIGH_ELF: "Gli elfi superiori hanno ricevuto la magia dei passaggi tra il Feywild ed il Piano Materiale. Su alcuni mondi, gli elfi superiori si fanno chiamare con altri nomi. Per esempio, usano il nome di elfi del sole o elfi della luna nell'ambientazione dei Reami Dimenticati, Silvanesti e Qualinesti in quella di Dragonlance ed Aereni nell'ambientazione di Eberron.",
    WOOD_ELF: "Gli elfi dei boschi portano in sé la magia di foreste primordiali. Sono conosciuti con molti altri nomi, tra cui elfi selvaggi, elfi verdi ed elfi delle foreste. I Grurach sono elfi dei boschi solitari dell'ambientazione di Greyhawk, mentre i Kagonesti ed i Tairnadal sono elfi dei boschi delle ambientazioni di Dragonlance ed Eberron, rispettivamente.",
    GNOME: "Gli gnomi sono creature magice create dagli dei delle invenzioni, delle illusioni e della vita sotterranea. I primi gnomi erano raramente visti da altri individui data la natura riservata degli gnomi e la loro propensione a vivere in foreste e tane. Ciò che non avevano in stazza, veniva compensato dall'intelligenza. Confondevano i predatori con trappole e tunnel labirintici. Inoltre, appresero la magia da dei come Garl Glittergold, Baervan Wildwanderer e Barabar Cloakshadow, che li visitavano sotto mentite spoglie. Tale magia finì per generare i lignaggi di gnomi delle foreste e gnomi delle rocce.<br>Gli gnomi sono gente minuta, con occhi grossi ed orecchie a punta, che vive circa 425 anni. Molti gnomi apprezzano la sensazione di un tetto sopra la testa, anche se tale 'tetto' non dovesse essere nulla di più di un cappello.",
    GOLIATH: "Torreggianti sulla maggior parte delle persone, i goliath sono lontani discendenti dei giganti. Ogni goliath porta in sé la benedizione dei primi giganti - benedizione che si manifesta in vari doni sovrannaturali, inclusa l'abilità di crescere rapidamente e avvicinarsi temporaneamente all'altezza della stirpe gigantica dei goliath.<br>I goliath possiedono caratteristiche fisiche che ricordano i giganti dei loro alberi genealogici. Per esempio, alcuni goliath somigliano ai giganti di pietra, mentre altri ricordano i giganti di fuoco. Quali che siano i giganti tra i loro antenati, i goliath hanno tracciato la propria strada nel multiverso - non ostacolati dai conflitti intestini che hanno afflitto i giganti per ere - e mirano a cime più alte di quelle raggiunte dai propri antenati.",
    HALFLING: "Amati e guidati dagli dei che apprezzano la vita, la case e la terra, gli halfling gravitano attorno a rifugi bucolici ove famiglia e comunità aiutano a dar forma alle loro vite. Detto ciò, molti halfling posseggono uno spirito coraggioso ed avventuroso che li conduce verso viaggi di scoperta, dando loro la possibilità di esplorare un mondo più grande e farsi nuovi amici lungo la strada. la loro stazza - simile a quella di un bambino umano - li aiuta ad attraversare le folle senza essere notati e ad infilarsi in spazi stretti.<br>Chiunque abbia passato del tempo in compagnia di halfling, in particolare avventurieri halfling, ha probabilmente assistito alla proverbiale 'fortuna degli halfling' in azione. Quando un halfling è in pericolo mortale, una forza invisibile sembra intervenire per suo conto. Molti halfling credono nel potere della fortuna, e attribuiscono il proprio dono insolito ad uno dei loro dei benevolenti, tra cui Yondalla, Brandobaris e Charmalaine. Lo stesso dono potrebbe contribuire alla loro robusta aspettativa di vita (circa 150 anni).<br>Ci sono comunità di Halfling di tutti i tipi. Per ogni contea solitaria in un incontaminato angolo del mondo, c'è un sindacato criminale come il Clan di Boromar nell'ambientazione di Eberron un una criminalità organizzata territoriale di halfling come quelli dell'ambientazione di Dark Sun.<br>Gli halfling che preferiscono vivere sottoterra sono a volte chiamati Halfling Cuoreforte o Halfling Tozzi. Gli halfling nomadi, come quelli che vivono tra gli uomini e altra gente alta, sono a volte chiamati Halfling Piedelesto o Alticompagni.",
    HUMAN: "Diffusi ovunque nel multiverso, gli umani sono tanto vari quanto numerosi, e si sforzano di ottenere tutto ciò che possono negli anni che sono loro concessi. Le loro ambizione e intraprendenza sono lodate, rispettate e temute su molti mondi.<br>Gli umani sono diversi nell'aspetto quanto la popolazione della Terra, e venerano molteplici dei. Gli studiosi sono incerti sull'origine dell'umanità, ma uno dei più antichi ritrovi conosciuti di umani si dice abbia avuto luogo a Sigil, la città dalla forma toroidale al centro del multiverso ed il luogo in cui è nata la lingua Comune. Da lì, gli umani si sono potuti diffondere in ogni parte del multiverso, portando la cosmopolia della Città delle Porte con sé.",
    ORC: "Gli orchi devono la propria creazione a Gruumsh, un potente dio che ha viaggiato per gli ampi spazi del Piano Materiale. Gruumsh ha dotato i propri figli di doni per aiutarli a viaggiare per grandi pianure, ampie caverne e mari tempestosi e ad affrontare i mostri che si nascondono in tali luoghi.<br>Gli orchi sono, solitamente, alti e robusti. Hanno la pelle grigia, orecchie a punta e canini inferiori sporgenti che ricordano piccole zanne. I giovani orchi di alcuni mondi sentono spesso parlare dei grandi viaggi e travagli dei propri antenati. Ispirati da tali racconti, molti di quegli orchi si domandano se Gruumsh chiederà loro di compiere imprese eroiche di pari valore e se si dimostreranno meritevoli del favore del dio. Altri orchi sono felici di lasciarsi alle spalle gli antichi racconti e trovare la propria strada.",
    TIEFLING: "I tiefling nascono nei Piani Inferiori o hanno antenati demoniaci originari di lì. Un tiefling ha un legame di sangue ocn un diavolo, un demone o qualche altro Demonio. Questa connessione ai Piani Inferiori si nota nell'eredità infernale del tiefling, che reca in sé la promessa di potere, ma non influenza la morale del tiefling.<br>Un tiefling decide se abbracciare o rinnegare la propria eredità infernale.",
    ABYSSAL_TIEFLING: "L'entropia dell'Abisso, il caos di Pandemonium e la disperazione di Carceri chiamano a sé i tiefling dall'eredità abissale. Corna, pelo, zanne e odori particolari sono caratteristiche fisiche tipiche di tali tiefling, nelle vene di molti dei quali scorre sangue di demone.",
    CHTONIC_TIEFLING: "I tiefling di eredità ctonia percepiscono non solo l'attrazione di Carceri, ma anche l'avidità di Gehenna e l'oscurità di Hades. Alcuni di questi tiefling hanno un aspetto cadaverico. Altri possiedono la bellezza extraterrena di una succube, o hanno caratteristiche fisiche in comune con una strega notturna, uno yugoloth o qualche altro antenato demoniaco Neutrale Malvagio.",
    INFERNAL_TIEFLING: "L'eredità infernale collega i tiefling non solo a Gehenna, ma anche ai Nove Inferi e i furiosi campi di battaglia di Acheron. Corna, spine, code, occhi dorati ed un lieve odore di zolfo o fumo sono caratteristiche fisiche comuni per tali tiefling, molti dei quali hanno dei diavoli tra i propri antenati.",
    
    BARBARIAN: "I Barbari sono potenti guerrieri che traggono il proprio potere da forze primordiali del multiverso, le quali si manifestano sotto forma di Ira. Più che una semplice emozione - e non limitata alla rabbia - questa Ira è un'incarnazione della ferocia di un predatore, della furia di una tempesta e del tumulto di un mare.<br>Alcuni Barbari vedono nella propria Ira un potente spirito od un antenato riverito. Altri la vivono come una connessione al dolore ed alla sofferenza del mondo, un groviglio impersonale di magia selvaggia, o come espressione del sé più profondo. Per ogni Barbaro, la propria Ira è un potere che alimenta non solo la prodezza in battaglia, ma anche riflessi inusuali e sensi acuiti.<br>I Barbari spesso servono come protettori e comandanti nelle proprie comunità. Si buttano a capofitto nel pericolo affinché coloro che proteggono non debbano farlo. Il loro coraggio di fronte al pericolo rende i Barbari perfettamente adatti all'avventura.",

    ACOLYTE: "Sei devoto al servizio in un tempio, si trovi esso nel mezzo di un paese o rintanato in un boschetto sacro. Lì hai eseguito rituali in onore di un dio o di un pantheon. Hai avuto un prete per maestro ed hai studiato religione. Grazie all'istruzione del tuo prete e alla tua devozione, hai anche imparato come incanalare un certo quantitativo di potere divino al servizio del tuo luogo di culto e della gente che pregava lì.",
    ARTISAN: "Sin da quando sei stato forte abbastanza da tenere in mano un secchiello, hai lavato pavimenti e pulito banconi nell'officina di un artigiano per un pugno di monete di bronzo al giorno. Quando sei diventato abbastanza grande da fare da apprendista, hai imparato a costruire da te semplici oggetti, così come a gestire l'occasionale cliente esigente. La tua professione ti ha anche dotato di un'occhio attento ai dettagli.",
    CHARLATAN: "Appena diventato grande abbastanza da poter ordinare un boccale di birra, ti sei subito trovato uno sgabello preferito in ogni taverna nel raggio di quindici chilometri dal tuo luogo di nascita. Nel percorso tra osteria ed abbeveratoio, hai imparato a predare sugli sfortunati alla ricerca di una o due bugie confortevoli - magari una finta pozione o registri di ascendenza falsificati.",
    CRIMINAL: "Sei sopravvissuto ad una vita in vicoli scuri, tagliando borse o derubando negozi. Forse eri parte di un piccolo gruppo di delinquenti con obiettivi comuni, che si guardavano le spalle a vicenda. O forse eri un lupo solitario, che doveva difendersi dalla gilda dei ladri locali e da criminali più spaventosi.",
    ENTERTAINER: "Hai trascorso gran parte della tua gioventù vagando per fiere ed eventi, svolgendo lavori particolari per musicisti e acrobati in cambio di lezioni. Potresti aver imparato come camminare su una fune, come suonare il liuto con uno stile distintivo, o come recitare poesie con impeccabile dizione. Ad oggi, vivi di applausi e desideri il palcoscenico.",
    FARMER: "Sei cresciuto a contatto con i campi. Gli anni passiati ad accudire animali e coltivare la terra ti hanno ricompensato con pazienza e buona salute. Possiedi un forte apprezzamento nei confronti dei doni della natura, oltre ad un salutare rispetto verso la sua ira.",
    GUARD: "I piedi ti dolgono quando ricordi le innumerevoli ore passate di guardia alla torre. Sei stato allenato a tenere d'occhio sia l'esterno delle mura, cercando briganti provenienti dalla foresta vicina, che l'interno, alla ricerca di tagliaborse e combinaguai.",
    GUIDE: "Hai raggiunto l'età adulta all'aria aperta, lontano dalla terra colonizzata. Casa tua si trovava ovunque tu scegliessi di distendere il tuo sacco a pelo. Ci sono meraviglie nella natura selvaggia - strani mostri, foreste e ruscelli incontaminati, rovine di grandi sale una volta calpestate dai giganti ed ora ricoperte dalla vegetazione - ed hai imparato a difenderti mentre le esplori. Talvolta, hai guidato amichevoli preti della natura che ti hanno insegnato i fondamenti dell'incanalare la magia selvaggia.",
    HERMIT: "Hai passato i primi anni di vita rinchiuso in una capanna o monastero situato ben oltre i sobborghi dell'accampamento più vicino. In tali giorni, i tuoi unici compagni erano le creature della foresta e coloro che occasionalmente facevano visita per portare notizie del mondo esterno e provviste. La solitudine ti ha permesso di passare molte ore a riflettere sui misteri della creazione.",
    MERCHANT: "Hai fatto da apprendista ad un commerciante, capocarovana o negoziante, apprendendo i fondamentali del commercio. Hai viaggiato in lungo e in largo, guadagnandoti da vivere comprando e vendendo materiali grezzi necessari agli artigiani per far pratica del proprio mestiere o i lavori finiti che questi producono. Puoi aver trasportato beni da un posto all'altro (via nave, carro o carovana) o averli comprati da commercianti itineranti per venderli nel tuo negozio.",
    NOBLE: "Sei cresciuto in un castello, circondato da ricchezza, potere e privilegi. La tua famiglia o degli aristocratici di basso rango si sono assicurati che ricevessi un'educazione di prima classe, che hai in parte apprezzato ed in parte disprezzato. Il tempo passato nel castello, in particolare le molteplici ore passate ad osservare la tua famiglia a corte, ti hanno anche insegnato molto riguardo i ruoli di comando.",
    SAGE: "Hai passato gli anni della tua formazione viaggiando tra manieri e monasteri, svolgendo vari lavori e servizi bizzarri in cambio di accesso alle loro biblioteche. Hai passato molte sere studiando libri e pergamene, apprendendo la storia del multiverso - anche i fondamenti della magia - e la tua mende anela a scoprire di più.",
    SAILOR: "Hai vissuto come navigatore, vento alle spalle e ponti che oscillano sotto i tuoi piedi. Ti sei appoggiato a sgabelli da bar in più porti di scalo di quanti tu ne possa ricordare, hai affrontato possenti tempeste e hai scambiato racconti con gente che vive al di sotto delle onde.",
    SCRIBE: "Hai passato gli anni della tua formazione in uno scriptorium, un monastero dedicato alla preservazione della gonoscenza, od un'agenzia governativa, dove hai imparato a scrivere con grafia leggibile ed a produrre testi scritti accuratamente. Forse stilavi documenti covernativi o copiavi tomi di letteratura. Potresti avere una certa abilità come scrittore di poesia, narrativa o ricerca accademica. Soprattutto, hai una fine attenzione ai dettagli, che ti aiuta ad evitare di introdurre errori nei documenti che copi e crei.",
    SOLDIER: "Hai iniziato ad addestrarti per la guerra non appena hai raggiunto l'età adulta e hai pochi preziosi ricordi della vita prima di imbracciare le armi. La battaglia è nel tuo sangue. A volte ti ritrovi ad eseguire istintivamente gli esercizi di combattimento basilare che hai imparato per primi. Alla fine, metti in uso quell'allenamento sul campo di battaglia, proteggendo il reame attraverso la guerra.",
    WAYFARER: "Sei cresciuto in mezzo alle strade circondato da scarti della società sfortunati quanto te, alcuni dei quali amici, altri rivali. Hai dormito dove potevi e svolto lavori bizzarri in cambio di cibo. A volte, quando la fame diveniva insopportabile, hai fatto ricorso al furto. Eppure, non hai mai perso il tuo orgoglio né abbandonato la speranza. Il destino ha ancora qualcosa in serbo per te.",

    QUICK_CRAFTING: "Attrezzi da Carpentiere:<br>Scala, Torica<br><br>Attrezzi da Conciatore:<br>Astuccio, Borsa<br><br>Attrezzi da Costruttore:<br>Paranco<br><br>Attrezzi da Ceramista:<br>Brocca, Lampada<br>Attrezzi da Fabbro:<br>Biglie, Secchio, Triboli, Rampino, Pentola di Ferro<br><br>Attrezzi da Armeggiatore:<br>Campanella, Pala, Scatola di Fiammiferi<br><br>Attrezzi da Tessitore:<br>Cesto, Fune, Rete, Tenda<br><br>Attrezzi da Intagliatore:<br>Randello, Randello Pesante, Bastone Ferrato",
    
    CLEAVE: "Se colpisci una creatura con un attacco da mischia utilizzando quest'arma, puoi fare un tiro di attacco con l'arma contro una seconda creatura entro 1,5 metri dalla prima che sia anch'essa entro la tua portata. Se colpita, la seconda creatura subisce il danno dell'arma, ma non aggiungere il tuo modificatore di abilità a tale danno a meno che il modificatore non sia negativo. Puoi usare questo attacco extra una sola volta per turno.",
    GRAZE: "Se il tuo tiro di attacco con questa arma manca una creatura, puoi infliggere danno a tale creatura pari al modificatore di abilità che hai utilizzato per il tiro di attacco. Tale danno è dello stesso tipo dell'arma, ed il danno può essere incrementato solo incrementando il modificatore di abilità.",
    NICK: "Quando usi l'attacco extra della proprietà Leggero, puoi usarlo come parte dell'azione di Attacco invece che come Azione Bonus. Puoi usare questo attacco extra una sola volta per turno.",
    PUSH: "Se colpisci una creatura con quest'arma, puoi spingere la creatura fino a 3 metri lontano da te in linea retta se è Larga o più piccola.",
    SAP: "Se colpisci una creatura con quest'arma, tale creature ha Svantaggio al proprio tiro di attacco successivo prima dell'inizio del tuo prossimo turno",
    SLOW: "Se colpisci una creatura con quest'arma e le infliggi danno, puoi ridurne la Velocità di 3 metri fino all'inizio del tuo prossimo turno. Se la creatura è colpita più di una volta da armi che hanno questa proprietà. la riduzione di Velocità non supera i 3 metri.",
    TOPPLE: "Se colpisci una creatura con quest'arma, puoi costringere la creatura a fare un tiro salvezza su Costituzione (DC 8 più il modificatore di abilità utilizzato per l'attacco ed il tuo Bonus Competenza). Se fallisce il tiro, la creatura subisce la condizione Prono.",
    VEX: "Se colpisci una creatura con quest'arma e le infliggi danno, hai Vantaggio al tuo prossimo tiro per colpire prima della fine del tuo prossimo turno."
    }

# Other acceptable values to access the dictionary. Could probably be improved, seems highly inefficient. In the case a word is contained inside another word, the longer word will be prioritized
SYNONYMS = { # To work properly, all synonims should be maximum 3 words long. If any longer are created, the text recognition needs to be updated
    "nano": DICTIONARY[DWARF],
    "Nani": DICTIONARY[DWARF],
    "nani": DICTIONARY[DWARF],
    "Nano delle Colline": DICTIONARY[DWARF],
    "nano delle colline": DICTIONARY[DWARF],
    "Nani delle Colline": DICTIONARY[DWARF],
    "nani delle colline": DICTIONARY[DWARF],
    "Nano delle Montagne": DICTIONARY[DWARF],
    "nano delle montagne": DICTIONARY[DWARF],
    "Nani delle Montagne": DICTIONARY[DWARF],
    "nani delle montagne": DICTIONARY[DWARF],
    "dragonide": DICTIONARY[DRAGONBORN],
    "Dragonidi": DICTIONARY[DRAGONBORN],
    "dragonidi": DICTIONARY[DRAGONBORN],
    "aasimar": DICTIONARY[AASIMAR],
    "drow": DICTIONARY[DROW],
    "elfo": DICTIONARY[ELF],
    "Elfi": DICTIONARY[ELF],
    "elfi": DICTIONARY[ELF],
    "elfo superiore": DICTIONARY[HIGH_ELF],
    "Elfi superiori": DICTIONARY[HIGH_ELF],
    "elfi superiori": DICTIONARY[HIGH_ELF],
    "elfo dei boschi": DICTIONARY[WOOD_ELF],
    "Elfi dei boschi": DICTIONARY[WOOD_ELF],
    "elfi dei boschi": DICTIONARY[WOOD_ELF],
    "Elfo Selvaggio": DICTIONARY[WOOD_ELF],
    "elfo selvaggio": DICTIONARY[WOOD_ELF],
    "Elfi Selvaggi": DICTIONARY[WOOD_ELF],
    "elfi selvaggi": DICTIONARY[WOOD_ELF],
    "Elfo Verde": DICTIONARY[WOOD_ELF],
    "elfo verde": DICTIONARY[WOOD_ELF],
    "Elfi Verdi": DICTIONARY[WOOD_ELF],
    "elfi verdi": DICTIONARY[WOOD_ELF],
    "Elfo delle Foreste": DICTIONARY[WOOD_ELF],
    "elfo delle foreste": DICTIONARY[WOOD_ELF],
    "Elfi delle Foreste": DICTIONARY[WOOD_ELF],
    "elfi delle foreste": DICTIONARY[WOOD_ELF],
    "Grurach": DICTIONARY[WOOD_ELF],
    "grurach": DICTIONARY[WOOD_ELF],
    "Kagonesti": DICTIONARY[WOOD_ELF],
    "kagonesti": DICTIONARY[WOOD_ELF],
    "Tairnadal": DICTIONARY[WOOD_ELF],
    "tairnadal": DICTIONARY[WOOD_ELF],
    "Elfo del Sole": DICTIONARY[HIGH_ELF],
    "elfo del sole": DICTIONARY[HIGH_ELF],
    "Elfi del Sole": DICTIONARY[HIGH_ELF],
    "elfi del sole": DICTIONARY[HIGH_ELF],
    "Elfo della Luna": DICTIONARY[HIGH_ELF],
    "elfo della luna": DICTIONARY[HIGH_ELF],
    "Elfi della Luna": DICTIONARY[HIGH_ELF],
    "elfi della luna": DICTIONARY[HIGH_ELF],
    "Silvanesti": DICTIONARY[HIGH_ELF],
    "silvanesti": DICTIONARY[HIGH_ELF],
    "Qualinesti": DICTIONARY[HIGH_ELF],
    "qualinesti": DICTIONARY[HIGH_ELF],
    "Aereni": DICTIONARY[HIGH_ELF],
    "aereni": DICTIONARY[HIGH_ELF],
    "Aereni": DICTIONARY[HIGH_ELF],
    "gnomo": DICTIONARY[GNOME],
    "Gnomi": DICTIONARY[GNOME],
    "gnomi": DICTIONARY[GNOME],
    "Gnomo delle Foreste": DICTIONARY[GNOME],
    "gnomo delle foreste": DICTIONARY[GNOME],
    "Gnomi delle Foreste": DICTIONARY[GNOME],
    "gnomi delle foreste": DICTIONARY[GNOME],
    "Gnomo delle Rocce": DICTIONARY[GNOME],
    "gnomo delle rocce": DICTIONARY[GNOME],
    "Gnomi delle Rocce": DICTIONARY[GNOME],
    "gnomi delle rocce": DICTIONARY[GNOME],
    "goliath": DICTIONARY[GOLIATH],
    "halfling": DICTIONARY[HALFLING],
    "Halfling Cuoreforte": DICTIONARY[HALFLING],
    "halfling cuoreforte": DICTIONARY[HALFLING],
    "Halfling Tozzo": DICTIONARY[HALFLING],
    "halfling tozzo": DICTIONARY[HALFLING],
    "Halfling Tozzi": DICTIONARY[HALFLING],
    "halfling tozzi": DICTIONARY[HALFLING],
    "Halfling Piedelesto": DICTIONARY[HALFLING],
    "halfling piedelesto": DICTIONARY[HALFLING],
    "Alticompagni": DICTIONARY[HALFLING],
    "alticompagni": DICTIONARY[HALFLING],
    "umano": DICTIONARY[HUMAN],
    "Umani": DICTIONARY[HUMAN],
    "umani": DICTIONARY[HUMAN],
    "Calishita": DICTIONARY[HUMAN],
    "calishita": DICTIONARY[HUMAN],
    "Chondathan": DICTIONARY[HUMAN],
    "chondathan": DICTIONARY[HUMAN],
    "Damaran": DICTIONARY[HUMAN],
    "damaran": DICTIONARY[HUMAN],
    "Illuskan": DICTIONARY[HUMAN],
    "illuskan": DICTIONARY[HUMAN],
    "Mulan": DICTIONARY[HUMAN],
    "mulan": DICTIONARY[HUMAN],
    "Rashemi": DICTIONARY[HUMAN],
    "rashemi": DICTIONARY[HUMAN],
    "Shou": DICTIONARY[HUMAN],
    "shou": DICTIONARY[HUMAN],
    "Tethyrian": DICTIONARY[HUMAN],
    "tethyrian": DICTIONARY[HUMAN],
    "Turami": DICTIONARY[HUMAN],
    "turami": DICTIONARY[HUMAN],
    "orco": DICTIONARY[ORC],
    "Orchi": DICTIONARY[ORC],
    "orchi": DICTIONARY[ORC],
    "tiefling": DICTIONARY[TIEFLING],
    "tiefling abissale": DICTIONARY[ABYSSAL_TIEFLING],
    "Tiefling Abissali": DICTIONARY[ABYSSAL_TIEFLING],
    "tiefling abissali": DICTIONARY[ABYSSAL_TIEFLING],
    "tiefling ctonio": DICTIONARY[CHTONIC_TIEFLING],
    "Tiefling Ctoni": DICTIONARY[CHTONIC_TIEFLING],
    "tiefling ctoni": DICTIONARY[CHTONIC_TIEFLING],
    "tiefling infernale": DICTIONARY[INFERNAL_TIEFLING],
    "Tiefling Infernali": DICTIONARY[INFERNAL_TIEFLING],
    "tiefling infernali": DICTIONARY[INFERNAL_TIEFLING],

    "Barbari": DICTIONARY[BARBARIAN],

    "accolito" : DICTIONARY[ACOLYTE],
    "Accoliti": DICTIONARY[ACOLYTE],
    "accoliti": DICTIONARY[ACOLYTE],
    "artigiano": DICTIONARY[ARTISAN],
    "Artigiani": DICTIONARY[ARTISAN],
    "artigiani": DICTIONARY[ARTISAN],
    "ciarlatano": DICTIONARY[CHARLATAN],
    "Ciarlatani": DICTIONARY[CHARLATAN],
    "ciarlatani": DICTIONARY[CHARLATAN],
    "criminale": DICTIONARY[CRIMINAL],
    "Criminali": DICTIONARY[CRIMINAL],
    "criminali": DICTIONARY[CRIMINAL],
    "intrattenitore": DICTIONARY[ENTERTAINER],
    "Intrattenitori": DICTIONARY[ENTERTAINER],
    "intrattenitori": DICTIONARY[ENTERTAINER],
    "contadino": DICTIONARY[FARMER],
    "Contadini": DICTIONARY[FARMER],
    "contadini": DICTIONARY[FARMER],
    "guardia": DICTIONARY[GUARD],
    "Guardie": DICTIONARY[GUARD],
    "guardie": DICTIONARY[GUARD],
    "guida": DICTIONARY[GUIDE],
    "Guide": DICTIONARY[GUIDE],
    "guide": DICTIONARY[GUIDE],
    "eremita": DICTIONARY[HERMIT],
    "Eremiti": DICTIONARY[HERMIT],
    "eremiti": DICTIONARY[HERMIT],
    "mercante": DICTIONARY[MERCHANT],
    "Mercanti": DICTIONARY[MERCHANT],
    "mercanti": DICTIONARY[MERCHANT],
    "nobile": DICTIONARY[NOBLE],
    "Nobili": DICTIONARY[NOBLE],
    "nobili": DICTIONARY[NOBLE],
    "saggio": DICTIONARY[SAGE],
    "Saggi": DICTIONARY[SAGE],
    "saggi": DICTIONARY[SAGE],
    "marinaio": DICTIONARY[SAILOR],
    "Marinai": DICTIONARY[SAILOR],
    "marinai": DICTIONARY[SAILOR],
    "scriba": DICTIONARY[SCRIBE],
    "Scribi": DICTIONARY[SCRIBE],
    "scribi": DICTIONARY[SCRIBE],
    "soldato": DICTIONARY[SOLDIER],
    "Soldati": DICTIONARY[SOLDIER],
    "soldati": DICTIONARY[SOLDIER],
    "viandante": DICTIONARY[WAYFARER],
    "Viandanti": DICTIONARY[WAYFARER],
    "viandanti": DICTIONARY[WAYFARER]
}

# Merge the main dictionary with synonyms
DICTIONARY = {**DICTIONARY, **SYNONYMS}


# Constants that are always valid
BASE_STATS = [15, 14, 13, 12, 10, 8]
LEVELS_BASE = [RANDOM] # Levels available when no class is chosen
LEVELS_NONE = ["0"] # Levels available when the class is "none"
LEVELS_CLASS = [RANDOM] + [str(i) for i in range(1, 21)] # Levels available for actual classes
LEVELS = { # Written like this because I may want to add specific levels for some classes in future. If a class is chosen, it defaults to LEVELS_CLASS (coded later on)
    RANDOM: LEVELS_BASE,
    NONE_F: LEVELS_NONE
}

# Setting level weights. Lower levels are WAY more common
# Step 1: Initialize the first value
initial_value = 524288  # Starting with 524288 to avoid very small numbers. Just useful for debugging, any number actually works

# Step 2: Set the common ratio
common_ratio = 0.5

# Step 3: Create a list to hold the probabilities
LEVELS_WEIGHTS = []

# Step 4: Generate the probabilities
for i in range(20):  # Assuming 20 levels
    LEVELS_WEIGHTS.append(int(initial_value * (common_ratio ** i)))


# Variables
character_info = { # Used to save characters in a file
    
    }


# Setting up accepptable values for lineEdits
class CustomIntValidator(QIntValidator):
    def validate(self, input, pos):
        if input == "":
            return (QValidator.State.Acceptable, input, pos)
        return super().validate(input, pos)
items_validator = CustomIntValidator(0, 1000)

regular_validator = QRegularExpressionValidator(QRegularExpression(r"[\w'À-ÖØ-öø-ÿ]+")) # Only letters, numbers, apostrophes and accented characters
abilities_validator = QIntValidator(0, 30)
number_validator = QIntValidator(0, 50)
integer_validator = QIntValidator(0, 1000)
health_validator = QRegularExpressionValidator(QRegularExpression(r"[0-9d\+]{0,8}")) # String of max 8 characters containing digits, d (for dice) or + (for sums)
double_validator = QDoubleValidator(0, 1000, 2)
double_validator.setNotation(QDoubleValidator.Notation.StandardNotation) # This is to avoid scientific notation
double_validator.setLocale(QLocale(QLocale.Language.English, QLocale.Country.UnitedStates)) # This is to avoid the comma as decimal separator
double_validator.setDecimals(2) # This is to allow only 2 decimal places


# Functions
# Creats the tooltip text to be later displayed
def create_tooltip(item):
    if item in TOOLTIPS:
        tooltip = TOOLTIPS[item]
    else:
        tooltip = ""

    # Split the tooltip text by existing newline characters
    lines = tooltip.split('\n')
    
    # Wrap each line individually
    wrapped_lines = [textwrap.fill(line, width=200, break_long_words=False, break_on_hyphens=False) for line in lines]
    
    # Join the wrapped lines with <br> for HTML line breaks
    wrapped_tooltip = "<br>".join(wrapped_lines)
    
    # Wrap the tooltip text in HTML tags
    if wrapped_tooltip:
        formatted_tooltip = f"<html><body>{wrapped_tooltip}</body></html>"
    else:
        formatted_tooltip = ""

    return formatted_tooltip


# Displays info contained inside the dictionary
def display_dictionary_info(item):
    if item in DICTIONARY:
        tab = QWidget()
        mainWindow.tabs.addTab(tab, item)
        mainWindow.tabs.setCurrentWidget(tab)
        layout = QVBoxLayout()
        label = HoverableLabel(DICTIONARY[item])
        label.setFixedWidth(int(scale_factor * 500))
        label.setWordWrap(True)
        layout.addWidget(label)
        tab.setLayout(layout)
        mainWindow.setFocus()


# Gets which word the cursor is hovering over
def get_word_under_cursor(text: str, cursor_pos: int) -> str:
    text = text.replace("<u>", "").replace("</u>", "").replace("<br>", " ")
    # Ensure cursor_pos is within the valid range
    if cursor_pos <= len(text):
        # Find the start of the word
        start = cursor_pos
        while start > 0 and text[start - 1].isalnum():
            start -= 1

        # Find the end of the word
        end = cursor_pos
        while end < len(text) and text[end].isalnum():
            end += 1
        
        # Get the single word under the cursor
        single_word = text[start:end]
        
        # Check for multi-word entries in the dictionary
        max_length = 3  # Maximum number of words to check for multi-word entries
        best_match = single_word
        
        # Expand forwards and backwards
        for length in range(2, max_length + 1):
            # Expand forwards
            extended_end = end
            for _ in range(length - 1):
                while extended_end < len(text) and text[extended_end].isspace():
                    extended_end += 1
                while extended_end < len(text) and (text[extended_end].isalnum() or text[extended_end] == "'"):
                    extended_end += 1
            multi_word_forward = text[start:extended_end].strip()
            if multi_word_forward in DICTIONARY and len(multi_word_forward) > len(best_match):
                best_match = multi_word_forward
            
            # Expand backwards
            extended_start = start
            for _ in range(length - 1):
                while extended_start > 0 and text[extended_start - 1].isspace():
                    extended_start -= 1
                while extended_start > 0 and (text[extended_start - 1].isalnum() or text[extended_start - 1] == "'"):
                    extended_start -= 1
            multi_word_backward = text[extended_start:end].strip()
            if multi_word_backward in DICTIONARY and len(multi_word_backward) > len(best_match):
                best_match = multi_word_backward

        # Expand both ways
        extended_end = end
        extended_start = start

        while extended_start > 0 and text[extended_start - 1].isspace():
            extended_start -= 1
        while extended_start > 0 and (text[extended_start - 1].isalnum() or text[extended_start - 1] == "'"):
            extended_start -= 1

        while extended_end < len(text) and text[extended_end].isspace():
            extended_end += 1
        while extended_end < len(text) and (text[extended_end].isalnum() or text[extended_end] == "'"):
            extended_end += 1

        multi_word = text[extended_start:extended_end].strip()

        if multi_word in DICTIONARY and len(multi_word) > len(best_match):
            best_match = multi_word
        
        # Return the best match found
        return best_match
    return ""


# Wraps the text to fit the label width
def get_wrapped_lines(text: str, font_metrics: QFontMetrics, label_width: int) -> list:
    lines = []
    text = text.replace("<u>", "").replace("</u>", "")
    paragraphs = text.split("<br>")
    
    for paragraph in paragraphs:
        # Measure the width of the entire paragraph
        if font_metrics.horizontalAdvance(paragraph) <= label_width:
            lines.append(paragraph)
        else:
            # If the paragraph is too wide, split it into words and wrap accordingly
            words = paragraph.split()
            current_line = ""
            for word in words:
                # Check if the word ends with punctuation
                if word[-1] in string.punctuation:
                    word_without_punct = word[:-1]
                else:
                    word_without_punct = word
                
                # Measure the width of the current line plus the word (excluding punctuation)
                if font_metrics.horizontalAdvance(current_line + word_without_punct) <= label_width:
                    current_line += f"{word} "
                else:
                    lines.append(current_line.strip())
                    current_line = f"{word} "
            
            if current_line:
                lines.append(current_line.strip())
    return lines


def get_cursor_position_from_mouse(label: QLabel) -> int:
    # Get the mouse position relative to the label
    mouse_pos = label.mapFromGlobal(QCursor.pos())
    # Calculate the approximate character position based on the mouse position and font metrics
    font_metrics = QFontMetrics(label.font())
    x = mouse_pos.x()
    y = mouse_pos.y()
    # Get the wrapped lines based on the label's width
    lines = get_wrapped_lines(label.text(), font_metrics, label.width())
    
    # Calculate the actual height of the text
    actual_text_height = len(lines) * font_metrics.height()
    
    # Adjust the mouse position to ensure it falls within the bounds of the actual text height
    y = (y - (label.height() - actual_text_height) / 2)
    
    # Ensure the mouse position is within the visible area of the label
    if y < 0 or y >= label.height():
        return -1  # Return an invalid position if the mouse is outside the visible area
    
    cursor_pos = 0
    for line in lines:
        line_height = font_metrics.height()
        if y < line_height:
            # Iterate through the text to find the character position
            for i in range(len(line)):
                if font_metrics.horizontalAdvance(line[:i + 1]) > x:
                    cursor_pos += i
                    return cursor_pos
            cursor_pos += len(line)
            return cursor_pos
        else:
            y -= line_height
            cursor_pos += len(line) + 1  # +1 for the newline character
    return cursor_pos


# Makes sure the input is valid. If not deletes the last character, making it look like the input was never made
def validate_input(lineEdit: QLineEdit, default=None):
    validator = lineEdit.validator()
    text = lineEdit.text()
    state, _, _ = validator.validate(text, 0)
    if state != QValidator.State.Acceptable:
        lineEdit.setText(text[:-1])
    if not text and default:
        lineEdit.setText(default)


# Classes for the PyQt elements.
# I want to be able to select the text that appears on screen and I want to show a tooltip while hovering over a word that is in a specific groups (like weapons or spells)
class HoverableLabel(QLabel):
    def __init__(self, text=""):
        super().__init__()
        for word in reversed(DICTIONARY):
            pattern = rf'\b{re.escape(word)}\b'
            text = re.sub(pattern, f"<u>{word}</u>", text)
        self.setText(text)
        self.setTextInteractionFlags(Qt.TextInteractionFlag.TextSelectableByMouse)
        self.setCursor(Qt.CursorShape.IBeamCursor)
        self.tooltip = create_tooltip(self.text().replace("<u>", "").replace("</u>", "").replace("*", ""))

    def enterEvent(self, _) -> None: # Activates when hovering over with the mouse
        self.setFocus() # Needed for keyPressEvent to work. Needs to be before the tooltip, otherwise the tooltip won't show
        QToolTip.showText(self.mapToGlobal(QPoint(-50, 10)), self.tooltip, self)

    def leaveEvent(self, _) -> None: # Lose focus when not hovering over anymore
        mainWindow.setFocus()

    def keyPressEvent(self, event: QKeyEvent) -> None:
        if event.key() == Qt.Key.Key_T:
            cursor_pos = get_cursor_position_from_mouse(self)
            text = self.text()
            word = get_word_under_cursor(text, cursor_pos)
            display_dictionary_info(word)


# Element used to make the tooltip appear for certain Qt elements
class ToolTipItemDelegate(QStyledItemDelegate):
    def helpEvent(self, event: QHelpEvent, view, _, index: QModelIndex):
        item = index.data(Qt.ItemDataRole.DisplayRole)
        tooltip = create_tooltip(item)
        QToolTip.showText(event.globalPos(), tooltip, view)

        return True
    

# Combobox that allows for hovering over the text and showing a tooltip
class HoverableComboBox(QComboBox):
    def __init__(self):
        super().__init__()
        self.wheelEnabled = True  # By default, wheel scrolling is enabled
        self.setStyleSheet("QComboBox::drop-down { width: 0px; }")
        self.setItemDelegate(ToolTipItemDelegate(self))

    def enterEvent(self, _) -> None:
        tooltip = create_tooltip(self.currentText())
        self.setFocus()
        QToolTip.showText(self.mapToGlobal(QPoint(-50, 10)), tooltip, self)

    def keyPressEvent(self, event: QKeyEvent) -> None:
        if event.key() == Qt.Key.Key_T:
            display_dictionary_info(self.currentText())

    def wheelEvent(self, event: QWheelEvent):
        if not self.wheelEnabled:
            event.ignore()  # Ignore the wheel event if wheelEnabled is False
        else:
            super().wheelEvent(event)  # Otherwise, proceed with the default behavior

    def enableWheel(self):
        self.wheelEnabled = True  # Enable wheel scrolling

    def disableWheel(self):
        self.wheelEnabled = False  # Disable wheel scrolling
            

# QCompleter without the ability to scroll through the options
class NoScrollCompleter(QCompleter):
    def __init__(self, items):
        super().__init__(items)
        self.popup().setItemDelegate(ToolTipItemDelegate(self))
    
    def eventFilter(self, obj, event):
        if isinstance(event, QKeyEvent):
            if event.key() in (Qt.Key.Key_Up, Qt.Key.Key_Down):
                return True  # Ignore the event
        return super().eventFilter(obj, event)


# Main type of tab
class CharacterTab(QWidget):
    def __init__(self, generator, character_info: Dict):
        super().__init__()

        self.character_info: Dict[str, Union[dict, str, int, List[list], bool]] = copy.deepcopy(character_info)
        
        file_path = os.path.join(BASE_PATH, f"{self.character_info["savename"]}.png")

        if os.path.isfile(file_path):
            self.picture = file_path
        else:
            self.picture = ""

        self.subTabs = QTabWidget()

        self.info_tab = QWidget()
        self.magic_tab = QWidget()
        self.extra_tab = QWidget()
        self.status_tab = QWidget()

        info_grid = QGridLayout()
        self.info_tab.setLayout(info_grid)

        magic_grid = QGridLayout()
        self.magic_tab.setLayout(magic_grid)

        extra_grid = QGridLayout()
        self.extra_tab.setLayout(extra_grid)

        status_grid = QGridLayout()
        self.status_tab.setLayout(status_grid)

        self.subTabs.addTab(self.info_tab, INFO_TRANSLATE)
        self.subTabs.addTab(self.magic_tab, MAGIC_TRANSLATE)
        self.subTabs.addTab(self.extra_tab, EXTRA_TRANSLATE)
        self.subTabs.addTab(self.status_tab, STATUS_TRANSLATE)
        self.subTabs.setTabPosition(QTabWidget.TabPosition.West)

        layout = QVBoxLayout(self)
        layout.addWidget(self.subTabs)
        self.setLayout(layout)

        self.saved = False
        self.raging = False
        self.savename = None
        self.abilitiesBonus = []
        self.advantage = 0
        self.proficiency = 1
        self.speed = 0

        self.attributes = {
            "Unarmored Defense": False
        }

        self.armor_speed = 0
        self.armor_disadvantages = []

        self.longRest_remove = []

        self.spell_widgets: List[List[HoverableLabel]] = [[]] * 10

        self.abilities = [10, 10, 10, 10, 10, 10]
        self.abilitiesBase = [10, 10, 10, 10, 10, 10]
        self.ability_bonuses = [0, 0, 0, 0, 0, 0]
        self.abilitiesFixed = [0, 0, 0, 0, 0, 0]
        self.abilitiesChanged = [0, 0, 0, 0, 0, 0]

        self.abilitiesFeats = [[0 for _ in range(6)] for _ in range(LEN_FEATS)]
        self.feats_misc = [[]] * LEN_FEATS
        self.feats_tool_proficiencies = [[]] * LEN_FEATS
        self.features = [[]] * LEN_FEATURES
        self.features_widgets: List[List[Union[str, HoverableComboBox, HoverableLabel]]] = [[]] * LEN_FEATURES * MAX_NUM
        self.features_resistances = [[]] * LEN_FEATURES * MAX_NUM
        self.features_save_advantages = [[]] * LEN_FEATURES * MAX_NUM
        self.features_throw_advantages = [[]] * LEN_FEATURES * MAX_NUM
        self.initiative_proficiency = [False] * LEN_FEATS

        self.skill_proficiencies_choices = []
        self.armor_proficiencies_fixed = []

        self.class_weapon_proficiencies = []
        self.class_armor_proficiencies = []
        self.class_tool_proficiencies = []
        self.class_skill_proficiencies = []
        self.class_condition_advantages = []
        self.class_condition_immunities = []
        self.class_save_advantages = []
        self.class_resistances = []
        self.class_misc = []
        self.class_magic = []
        self.class_equipment = []

        self.subclass_weapon_proficiencies = []
        self.subclass_armor_proficiencies = []
        self.subclass_tool_proficiencies = []
        self.subclass_skill_proficiencies = []
        self.subclass_condition_advantages = []
        self.subclass_condition_immunities = []
        self.subclass_save_advantages = []
        self.subclass_resistances = []
        self.subclass_misc = []
        self.subclass_magic = []

        self.background_tool_proficiencies = []
        self.background_skill_proficiencies = []
        self.background_equipment = []
        self.background_feat = ""

        self.species_weapon_proficiencies = []
        self.species_armor_proficiencies = []
        self.species_tool_proficiencies = []
        self.species_skill_proficiencies = []
        self.species_condition_advantages = []
        self.species_condition_immunities = []
        self.species_save_advantages = []
        self.species_resistances = []
        self.species_misc = []
        self.species_magic = []
        self.species_ageMax = 0
        self.species_height_base = 0
        self.species_weight_base = 0
        self.species_height_modifier = 0
        self.species_weight_modifier = 0
        self.species_namesMale = []
        self.species_namesFemale = []
        self.species_namesLast = []
        self.species_healthBase = 0
        self.species_healthBonus = 0
        self.species_languages = []

        self.lineage_weapon_proficiencies = []
        self.lineage_armor_proficiencies = []
        self.lineage_tool_proficiencies = []
        self.lineage_skill_proficiencies = []
        self.lineage_condition_advantages = []
        self.lineage_condition_immunities = []
        self.lineage_save_advantages = []
        self.lineage_resistances = []
        self.lineage_misc = []
        self.lineage_magic = []
        self.lineage_height_base = 0
        self.lineage_weight_base = 0
        self.lineage_height_modifier = 0
        self.lineage_weight_modifier = 0
        self.species_speed = 0
        self.lineage_speed = 0
        self.species_darkvision = 0
        self.lineage_darkvision = 0
        self.lineage_namesMale = []
        self.lineage_namesFemale = []
        self.lineage_namesLast = []
        self.lineage_healthBase = 0
        self.lineage_healthBonus = 0
        self.lineage_languages = []

        self.addedCopper = 0
        self.addedSilver = 0
        self.addedElectrum = 0
        self.addedGold = 0
        self.addedPlatinum = 0
        
        self.developmentAge = 0
        self.weapon_proficiencies = []
        self.healthBonus = 0
        self.skill_proficiencies_choices = []
        self.skill_proficiencies_fixed = []
        self.hitDie = 0
        self.hitDieFixed = 0
        self.choosingFeat = False
        self.extra_equipment = ""
        self.extra_comboBox = None
        self.equipmentChoice_comboBoxes: List[List[QComboBox]] = [[], []]

        self.languages_widgets: List[Union[QLabel, QComboBox]] = []
        self.languages = []
        self.proficiencies_widgets: List[Union[QLabel, QComboBox]] = []
        self.proficiencies = []
        self.equipment_widgets: List[Dict[str, Union[QCheckBox, QLineEdit, QLabel, QPushButton, QComboBox]]] = []

        self.points = 27 # Needed for the point buy system

        abilities_groupBox = QGroupBox(ABILITIES_TRANSLATE)
        abilities_grid = QGridLayout()
        abilities_groupBox.setLayout(abilities_grid)
        abilities_groupBox.setMinimumWidth(int(20 + scale_factor * 600))

        saves_groupBox = QGroupBox(SAVES_TRANSLATE)
        saves_grid = QGridLayout()
        saves_groupBox.setLayout(saves_grid)
        abilities_grid.addWidget(saves_groupBox, 13, 0, 5, 6)

        self.abilities_widgets: Dict[str, Dict[str, Union[HoverableLabel, QLineEdit, QCheckBox, QPushButton, QComboBox]]] = {}

        for ability in ABILITIES:
            label = HoverableLabel(f"{ability} (0)")
            base_lineEdit = QLineEdit() # Value without modifiers
            base_label = QLabel()
            base_label.setStyleSheet("color: gray;")
            base_comboBox = HoverableComboBox()
            base_comboBox.addItems(ABILITIES_CHOICES)
            base_comboBox.setFixedWidth(int(10 + scale_factor * 70))
            basePoint_label = QLabel("8")
            minus_button = QPushButton("-")
            minus_button.setFixedWidth(int(scale_factor * 20))
            minus_button.clicked.connect(lambda _, a=ability: self.minus_points(a))
            plus_button = QPushButton("+")
            plus_button.setFixedWidth(int(scale_factor * 20))
            plus_button.clicked.connect(lambda _, a=ability: self.plus_points(a))

            final_label = QLabel() # Value with modifiers
            final_label.setStyleSheet("color: gray;")

            base_lineEdit.setValidator(abilities_validator)

            checkBox1 = QCheckBox("+1")
            checkBox2 = QCheckBox("+2")

            saves_label = HoverableLabel(f"{ability}: 0")
            checkBox = QCheckBox()
            button = QPushButton(THROW_TRANSLATE, default=False, autoDefault=False)

            self.abilities_widgets[ability] = { # Self items are used later on. Items that always stay the same don't use self
                "label": label,
                "base_lineEdit": base_lineEdit,
                "base_label": base_label,
                "base_comboBox": base_comboBox,
                "basePoint_label": basePoint_label,
                "minus_button": minus_button,
                "plus_button": plus_button,
                "final_label": final_label,
                "checkBox1": checkBox1,
                "checkBox2": checkBox2,

                "saves_label": saves_label,
                "checkBox": checkBox,
                "button": button
            }

            base_lineEdit.hide()
            base_label.hide()
            basePoint_label.hide()
            minus_button.hide()
            plus_button.hide()

            abilities_grid.addWidget(label, ABILITIES.index(ability) * 2, 3)
            abilities_grid.addWidget(base_lineEdit, ABILITIES.index(ability) * 2 + 1, 3)
            abilities_grid.addWidget(base_label, ABILITIES.index(ability) * 2 + 1, 3)
            abilities_grid.addWidget(base_comboBox, ABILITIES.index(ability) * 2 + 1, 3)
            abilities_grid.addWidget(basePoint_label, ABILITIES.index(ability) * 2 + 1, 3)
            abilities_grid.addWidget(minus_button, ABILITIES.index(ability) * 2 + 1, 2)
            abilities_grid.addWidget(plus_button, ABILITIES.index(ability) * 2 + 1, 4)
            abilities_grid.addWidget(final_label, ABILITIES.index(ability) * 2 + 1, 5)
            abilities_grid.addWidget(checkBox1, ABILITIES.index(ability) * 2 + 1, 0)
            abilities_grid.addWidget(checkBox2, ABILITIES.index(ability) * 2 + 1, 1)

            saves_grid.addWidget(checkBox, ABILITIES.index(ability)%3, int(np.floor(ABILITIES.index(ability) / 3) * 3))
            saves_grid.addWidget(saves_label, ABILITIES.index(ability)%3, int(np.floor(ABILITIES.index(ability) / 3) * 3 + 1))
            saves_grid.addWidget(button, ABILITIES.index(ability)%3, int(np.floor(ABILITIES.index(ability) / 3) * 3 + 2))

            label.setFixedWidth(int(scale_factor * 90))
            base_lineEdit.setFixedWidth(int(scale_factor * 30))
            final_label.setFixedWidth(int(scale_factor * 20))

            checkBox1.setEnabled(False)
            checkBox2.setEnabled(False)

            saves_label.setFixedWidth(int(scale_factor * 130))
            checkBox.setEnabled(False)

            button.clicked.connect(lambda _, l=saves_label: self.dice(int(l.text().split(": ")[1])))
            base_lineEdit.editingFinished.connect(self.update_abilities)
            base_lineEdit.textEdited.connect(lambda _, b=base_lineEdit: validate_input(b))
            base_comboBox.activated.connect(self.update_abilities)
            minus_button.clicked.connect(lambda _, a=ability: self.minus_points(a))
            plus_button.clicked.connect(lambda _, a=ability: self.plus_points(a))
            checkBox1.clicked.connect(lambda _, a=ability: self.update_ability_bonus(a, "1"))
            checkBox2.clicked.connect(lambda _, a=ability: self.update_ability_bonus(a, "2"))
            checkBox.clicked.connect(lambda _, a=ability: self.update_ability_save(a))
            
            button.setFixedWidth(int(scale_factor * 30))
        
        self.points_label = QLabel()
        abilities_grid.addWidget(self.points_label, 0, 1)
        self.points_label.hide()

        skills_groupBox = QGroupBox(SKILLS_TRANSLATE)
        skills_grid = QGridLayout()
        skills_groupBox.setLayout(skills_grid)
        abilities_grid.addWidget(skills_groupBox, 0, 6, 18, 4)

        self.skills_widgets: Dict[str, Dict[str, Union[HoverableLabel, QPushButton, QCheckBox, list]]] = {}

        for skill in SKILLS:
            label = HoverableLabel(f"{skill}: 0")
            button = QPushButton(THROW_TRANSLATE, default=False, autoDefault=False)
            checkBox = QCheckBox()

            self.skills_widgets[skill] = {
                "label": label,
                "button": button,
                "checkBox": checkBox,
                "checkable": []
            }

            checkBox.setEnabled(False)

            button.clicked.connect(lambda _, l=label: self.dice(int(l.text().split(": ")[1]), "1d20", 0,  None, False, self.character_info[POISONED]))
            checkBox.clicked.connect(lambda _, s=skill: self.update_skills(s))

            skills_grid.addWidget(checkBox, list(SKILLS.keys()).index(skill), 0)
            skills_grid.addWidget(label, list(SKILLS.keys()).index(skill), 1)
            skills_grid.addWidget(button, list(SKILLS.keys()).index(skill), 2)
            label.setFixedWidth(int(scale_factor * 120))
            button.setFixedWidth(int(scale_factor * 30))

        health_groupBox = QGroupBox(HEALTH_TRANSLATE)
        health_grid = QGridLayout()
        health_groupBox.setLayout(health_grid)
        health_groupBox.setMinimumWidth(int(scale_factor * 250))

        health_label = HoverableLabel(f"{HIT_POINTS_TRANSLATE}:")
        self.health_lineEdit = QLineEdit()
        self.healthCurrent_lineEdit = QLineEdit()
        self.healthCurrent_lineEdit.hide()
        self.health_checkBox = QCheckBox(MEDIUM_HP_TRANSLATE)
        self.health_lineEdit.setValidator(health_validator)
        self.healthCurrent_lineEdit.setValidator(integer_validator)

        self.armorClass_label = HoverableLabel(f"{ARMOR_CLASS_TRANSLATE}:")

        self.ProficiencyBonus_label = HoverableLabel(f"{PROFICIENCIES_BONUS_TRANSLATE}: 1")

        self.initiative_checkBox = QCheckBox()
        self.initiative_checkBox.setEnabled(False)
        self.initiative_checkBox.hide()
        self.initiative_label = HoverableLabel(f"{INITIATIVE_BONUS_TRANSLATE}: 0")
        self.initiative_button = QPushButton(THROW_TRANSLATE, default=False, autoDefault=False)
        self.initiative_button.clicked.connect(lambda: self.dice(int(self.initiative_label.text().split(": ")[1]), "1d20", 0, None, self.character_info[INVISIBLE], self.character_info[INCAPACITATED]))
        self.initiative_button.setFixedWidth(int(scale_factor * 30))

        self.passivePerception_label = HoverableLabel(f"{PASSIVE_PERCEPTION_TRANSLATE}: 10")

        self.size_label = HoverableLabel(f"{SIZE_TRANSLATE}: ")

        health_grid.addWidget(health_label, 1, 0)
        health_grid.addWidget(self.health_lineEdit, 1, 1)
        health_grid.addWidget(self.healthCurrent_lineEdit, 1, 2)
        health_grid.addWidget(self.health_checkBox, 0, 0)
        health_grid.addWidget(self.armorClass_label, 2, 0)
        health_grid.addWidget(self.ProficiencyBonus_label, 3, 0)
        health_grid.addWidget(self.initiative_checkBox, 4, 2)
        health_grid.addWidget(self.initiative_label, 4, 0)
        health_grid.addWidget(self.initiative_button, 4, 1)
        health_grid.addWidget(self.passivePerception_label, 5, 0)
        health_grid.addWidget(self.size_label, 6, 0)

        parameters_groupBox = QGroupBox(PARAMETERS_TRANSLATE)
        parameters_vBox = QVBoxLayout()
        parameters_groupBox.setLayout(parameters_vBox)
        parameters_groupBox.setMinimumWidth(int(scale_factor * 180))

        name_label = HoverableLabel(NAME_TRANSLATE)
        self.name_lineEdit = QLineEdit()
        self.name_lineEdit.setValidator(regular_validator)
        self.name_lineEdit.setMaxLength(27)

        gender_label = HoverableLabel(GENDER_TRANSLATE)
        self.gender_comboBox = HoverableComboBox()
        self.gender_comboBox.addItems(GENDERS)

        species_label = HoverableLabel(SPECIES_TRANSLATE)
        self.species_comboBox = HoverableComboBox()
        self.species_comboBox.addItems(SPECIES)

        self.lineage_label = HoverableLabel(LINEAGE_TRANSLATE)
        self.lineage_label.hide()
        self.lineage_comboBox = HoverableComboBox()
        self.lineage_comboBox.addItems(SPECIES[RANDOM]["lineages"])
        self.lineage_comboBox.hide()

        background_label = HoverableLabel(BACKGROUND_TRANSLATE)
        self.background_comboBox = HoverableComboBox()
        self.background_comboBox.addItems(BACKGROUNDS)

        self.speed_label = HoverableLabel(SPEED_TRANSLATE + ":")
        self.flySpeed_label = HoverableLabel()
        self.flySpeed_label.hide()

        self.darkvision_label = HoverableLabel(DARKVISION_TRANSLATE + ":")

        alignment_label = HoverableLabel(ALIGNMENT_TRANSLATE)
        self.alignment_comboBox = HoverableComboBox()
        self.alignment_comboBox.addItems(ALIGNMENTS)

        parameters_vBox.addWidget(name_label)
        parameters_vBox.addWidget(self.name_lineEdit)
        parameters_vBox.addWidget(gender_label)
        parameters_vBox.addWidget(self.gender_comboBox)
        parameters_vBox.addWidget(species_label)
        parameters_vBox.addWidget(self.species_comboBox)
        parameters_vBox.addWidget(self.lineage_label)
        parameters_vBox.addWidget(self.lineage_comboBox)
        parameters_vBox.addWidget(background_label)
        parameters_vBox.addWidget(self.background_comboBox)
        parameters_vBox.addWidget(alignment_label)
        parameters_vBox.addWidget(self.alignment_comboBox)
        parameters_vBox.addWidget(self.speed_label)
        parameters_vBox.addWidget(self.flySpeed_label)
        parameters_vBox.addWidget(self.darkvision_label)

        class_groupBox = QGroupBox(CLASS_TRANSLATE)
        class_grid = QGridLayout()
        class_grid.setSpacing(10)
        class_groupBox.setLayout(class_grid)

        class_scrollArea = QScrollArea()
        class_scrollArea.setWidget(class_groupBox)
        class_scrollArea.setWidgetResizable(True)
        
        class_label = HoverableLabel(CLASS_TRANSLATE)
        class_label.setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Fixed)
        self.class_comboBox = HoverableComboBox()
        self.class_comboBox.addItems(CLASSES)
        self.class_comboBox.setFixedWidth(int(scale_factor * 140))

        self.subclass_label = HoverableLabel(SUBCLASS_TRANSLATE)
        self.subclass_label.setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Fixed)
        self.subclass_label.hide()
        self.subclass_comboBox = HoverableComboBox()
        self.subclass_comboBox.addItems(CLASSES[RANDOM]["subclasses"])
        self.subclass_comboBox.hide()

        self.level_label = HoverableLabel(LEVEL_TRANSLATE)
        self.level_label.setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Fixed)
        self.level_label.hide()
        self.level_comboBox = HoverableComboBox()
        self.level_comboBox.addItems(LEVELS_BASE)
        self.level_comboBox.hide()

        self.levelUp_button = QPushButton(LEVEL_UP_TRANSLATE, default=False, autoDefault=False)
        self.levelUp_button.clicked.connect(self.level_up)
        self.levelUp_button.hide()

        class_grid.addWidget(class_label, 0, 0)
        class_grid.addWidget(self.class_comboBox, 1, 0)
        class_grid.addWidget(self.subclass_label, 2, 0)
        class_grid.addWidget(self.subclass_comboBox, 3, 0)
        class_grid.addWidget(self.level_label, 4, 0)
        class_grid.addWidget(self.level_comboBox, 5, 0)

        self.feats_widgets: Dict[int, Dict[str, Union[HoverableLabel, HoverableComboBox]]] = {}

        for i in range(LEN_FEATS):
            label.setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Fixed)
            comboBox = HoverableComboBox()
            comboBox.addItems(FEATS)
            comboBox1 = HoverableComboBox()
            comboBox1.addItems(ABILITIES_CHOICES)
            comboBox2 = HoverableComboBox()
            comboBox2.addItems(ABILITIES_CHOICES)
            
            if i > 1:
                label = HoverableLabel(f"{FEAT_TRANSLATE} {i-1}")
            elif i:
                label = HoverableLabel(f"{ORIGIN_FEAT} {EXTRA_TRANSLATE}")
            else:
                label = HoverableLabel(ORIGIN_FEAT)
                comboBox.setEditable(True)
                comboBox.lineEdit().setReadOnly(True)
                comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                comboBox.disableWheel()
                font = comboBox.lineEdit().font()
                font.setPointSize(int(12 * scale_factor))
                comboBox.lineEdit().setFont(font)

            self.feats_widgets[i] = {
                "label": label,
                "comboBox": comboBox,
                "comboBox1": comboBox1,
                "comboBox2": comboBox2
            }

            comboBox.setFixedWidth(int(scale_factor * 160))
            comboBox1.setFixedWidth(int(scale_factor * 90))
            comboBox2.setFixedWidth(int(scale_factor * 90))

            label.hide()
            comboBox.hide()
            comboBox1.hide()
            comboBox2.hide()

            class_grid.addWidget(label, 6 + 2 * i, 0)
            class_grid.addWidget(comboBox, 7 + 2 * i, 0)
            class_grid.addWidget(comboBox1, 7 + 2 * i, 1)
            class_grid.addWidget(comboBox2, 7 + 2 * i, 2)

            comboBox.activated.connect(lambda _, n=i: self.update_feat(n))
            comboBox1.activated.connect(lambda _, n=i: self.update_feat(n))
            comboBox2.activated.connect(lambda _, n=i: self.update_feat(n))


        class_grid.addWidget(self.levelUp_button)

        looks_groupBox = QGroupBox(LOOKS_TRANSLATE)
        looks_vBox = QVBoxLayout()
        looks_groupBox.setLayout(looks_vBox)
        looks_groupBox.setFixedWidth(int(scale_factor * 200))
        looks_groupBox.setFixedHeight(int(scale_factor * 300))

        age_label = HoverableLabel(AGE_TRANSLATE)
        self.age_lineEdit = QLineEdit()
        self.age_lineEdit.setValidator(integer_validator)

        self.ageMin_label = HoverableLabel(AGE_MIN_TRANSLATE)
        self.ageMin_lineEdit = QLineEdit()
        self.ageMin_lineEdit.setValidator(integer_validator)

        self.ageMax_label = HoverableLabel(AGE_MAX_TRANSLATE)
        self.ageMax_lineEdit = QLineEdit()
        self.ageMax_lineEdit.setValidator(integer_validator)

        height_label = HoverableLabel(f"{HEIGHT_TRANSLATE} [{LENGTH_UNIT}]")
        self.height_lineEdit = QLineEdit()
        self.height_lineEdit.setValidator(double_validator)

        weight_label = HoverableLabel(f"{WEIGHT_TRANSLATE} [{WEIGHT_UNIT}]")
        self.weight_lineEdit = QLineEdit()
        self.weight_lineEdit.setValidator(double_validator)

        looks_vBox.addWidget(age_label)
        looks_vBox.addWidget(self.age_lineEdit)
        looks_vBox.addWidget(self.ageMin_label)
        looks_vBox.addWidget(self.ageMin_lineEdit)
        looks_vBox.addWidget(self.ageMax_label)
        looks_vBox.addWidget(self.ageMax_lineEdit)
        looks_vBox.addWidget(height_label)
        looks_vBox.addWidget(self.height_lineEdit)
        looks_vBox.addWidget(weight_label)
        looks_vBox.addWidget(self.weight_lineEdit)

        proficiencies_groupBox = QGroupBox(PROFICIENCIES_TRANSLATE)
        proficiencies_grid = QGridLayout()
        proficiencies_groupBox.setLayout(proficiencies_grid)
        self.proficiencies_scrollArea = QScrollArea()
        self.proficiencies_scrollArea.setWidget(proficiencies_groupBox)
        self.proficiencies_scrollArea.setWidgetResizable(True)
        self.proficiencies_scrollArea.setMinimumWidth(int(75 + scale_factor * 560))

        languages_groupBox = QGroupBox(LANGUAGES_TRANSLATE)
        self.languages_grid = QGridLayout()
        languages_groupBox.setLayout(self.languages_grid)
        proficiencies_grid.addWidget(languages_groupBox, 0, 0, 1, 10)

        weapons_groupBox = QGroupBox(WEAPONS_ARMORS_TOOLS_TRANSLATE)
        proficiencies_grid.addWidget(weapons_groupBox, 1, 0, 1, 10)
        self.proficiencies_grid = QGridLayout()
        self.weapons_label = HoverableLabel()
        self.weapons_label.setWordWrap(True)
        self.proficiencies_grid.addWidget(self.weapons_label)
        weapons_groupBox.setLayout(self.proficiencies_grid)

        resistances_groupBox = QGroupBox(RESISTANCES_IMMUNITIES_TRANSLATE)
        proficiencies_grid.addWidget(resistances_groupBox, 2, 0, 1, 10)
        self.resistances_vBox = QVBoxLayout()
        self.resistances_label = HoverableLabel()
        self.resistances_label.setWordWrap(True)
        self.resistances_vBox.addWidget(self.resistances_label)
        resistances_groupBox.setLayout(self.resistances_vBox)

        misc_groupBox = QGroupBox(MISC_TRANSLATE)
        proficiencies_grid.addWidget(misc_groupBox, 3, 0, 1, 10)
        self.misc_vBox = QVBoxLayout()
        self.misc_label = HoverableLabel()
        self.misc_label.setWordWrap(True)
        self.misc_vBox.addWidget(self.misc_label)
        misc_groupBox.setLayout(self.misc_vBox)

        features_groupBox = QGroupBox(FEATURES_TRANSLATE)
        proficiencies_grid.addWidget(features_groupBox, 4, 0, 1, 10)
        self.features_grid = QGridLayout()
        features_groupBox.setLayout(self.features_grid)

        copper_label = HoverableLabel(f"            {COPPER}:")
        proficiencies_grid.addWidget(copper_label, 5, 0)
        self.copper_lineEdit = QLineEdit("0")
        self.copper_lineEdit.setValidator(integer_validator)
        self.copper_lineEdit.setFixedWidth(int(scale_factor * 45))
        proficiencies_grid.addWidget(self.copper_lineEdit, 5, 1)

        silver_label = HoverableLabel(f"            {SILVER}:")
        proficiencies_grid.addWidget(silver_label, 5, 2)
        self.silver_lineEdit = QLineEdit("0")
        self.silver_lineEdit.setValidator(integer_validator)
        self.silver_lineEdit.setFixedWidth(int(scale_factor * 45))
        proficiencies_grid.addWidget(self.silver_lineEdit, 5, 3)

        electrum_label = HoverableLabel(f"            {ELECTRUM}:")
        proficiencies_grid.addWidget(electrum_label, 5, 4)
        self.electrum_lineEdit = QLineEdit("0")
        self.electrum_lineEdit.setValidator(integer_validator)
        self.electrum_lineEdit.setFixedWidth(int(scale_factor * 45))
        proficiencies_grid.addWidget(self.electrum_lineEdit, 5, 5)

        gold_label = HoverableLabel(f"            {GOLD}:")
        proficiencies_grid.addWidget(gold_label, 5, 6)
        self.gold_lineEdit = QLineEdit("0")
        self.gold_lineEdit.setValidator(integer_validator)
        self.gold_lineEdit.setFixedWidth(int(scale_factor * 45))
        proficiencies_grid.addWidget(self.gold_lineEdit, 5, 7)

        platinum_label = HoverableLabel(f"            {PLATINUM}:")
        proficiencies_grid.addWidget(platinum_label, 5, 8)
        self.platinum_lineEdit = QLineEdit("0")
        self.platinum_lineEdit.setValidator(integer_validator)
        self.platinum_lineEdit.setFixedWidth(int(scale_factor * 45))
        proficiencies_grid.addWidget(self.platinum_lineEdit, 5, 9)

        self.equipmentFixed_checkBox = QCheckBox(BLOCK_EQUIPMENT_TRANSLATE)
        proficiencies_grid.addWidget(self.equipmentFixed_checkBox, 6, 0, 1, 4)

        finesse_label = HoverableLabel(FINESSE_ABILITY_TRANSLATE)
        proficiencies_grid.addWidget(finesse_label, 6, 5, 1, 3)

        self.finesse_comboBox = HoverableComboBox()
        self.finesse_comboBox.addItems([HIGHEST, STRENGTH, DEXTERITY])
        proficiencies_grid.addWidget(self.finesse_comboBox, 6, 8, 1, 2)

        equipment_groupBox = QGroupBox(EQUIPMENT_TRANSLATE)
        proficiencies_grid.addWidget(equipment_groupBox, 7, 0, 1, 10)
        self.equipment_grid= QGridLayout()
        equipment_groupBox.setLayout(self.equipment_grid)

        # LineEdit to add new equipment
        self.equipment_lineEdit = QLineEdit()
        proficiencies_grid.addWidget(self.equipment_lineEdit, 8, 0, 1, 10)
        self.equipment_lineEdit.setPlaceholderText(EQUIPMENT_NEW_TRANSLATE)

        # Extract the keys from each dictionary
        weapon_keys = list(WEAPONS.keys())
        armor_keys = list(ARMORS.keys())

        # Sort each list of keys
        weapon_keys.sort()
        armor_keys.sort()

        # Combine the sorted lists
        all_items = weapon_keys + armor_keys

        # Add the sorted items to the completer
        completer = NoScrollCompleter(all_items)
        completer.setCaseSensitivity(Qt.CaseSensitivity.CaseInsensitive)
        self.equipment_lineEdit.setCompleter(completer)

        self.equipment_lineEdit.editingFinished.connect(lambda: self.add_equipment(self.equipment_lineEdit.text(), True))

        system_groupBox = QGroupBox(SYSTEM_TRANSLATE)
        system_grid = QGridLayout()
        system_groupBox.setLayout(system_grid)
        system_groupBox.setMinimumWidth(int(50 + scale_factor * 560))

        self.edit_button = QPushButton(EDIT_TRANSLATE, default=False, autoDefault=False)
        system_grid.addWidget(self.edit_button, 0, 1)
        self.edit_button.setFixedWidth(int(scale_factor * 100))
        self.edit_button.hide()

        self.finish_button = QPushButton(FINISH_TRANSLATE, default=False, autoDefault=False)
        system_grid.addWidget(self.finish_button, 0, 1)
        self.finish_button.setFixedWidth(int(scale_factor * 100))
        self.finish_button.hide()

        self.confirm_button = QPushButton(CONFIRM_TRANSLATE, default=False, autoDefault=False)
        system_grid.addWidget(self.confirm_button, 0, 0)
        self.confirm_button.setFixedWidth(int(scale_factor * 100))

        self.save_button = QPushButton(SAVE_TRANSLATE, default=False, autoDefault=False)
        system_grid.addWidget(self.save_button, 0, 0)
        self.save_button.setFixedWidth(int(scale_factor * 100))
        self.save_button.hide()

        self.saveAs_button = QPushButton(SAVE_AS_TRANSLATE, default=False, autoDefault=False)
        system_grid.addWidget(self.saveAs_button, 1, 0)
        self.saveAs_button.setFixedWidth(int(scale_factor * 100))
        self.saveAs_button.hide()

        self.customization_groupBox = QGroupBox(CUSTOMIZATION_TRANSLATE)
        customization_grid = QGridLayout()
        self.customization_groupBox.setLayout(customization_grid)

        self.abilityGeneration_label = HoverableLabel(STAT_GENERATION_TRANSLATE)
        customization_grid.addWidget(self.abilityGeneration_label, 0, 0)

        self.confirmFeats_button = QPushButton(CONFIRM_FEATS, default=False, autoDefault=False)
        customization_grid.addWidget(self.confirmFeats_button, 1, 0)
        self.confirmFeats_button.setFixedWidth(int(scale_factor * 100))
        self.confirmFeats_button.hide()

        self.abilityGeneration_comboBox = HoverableComboBox()
        self.abilityGeneration_comboBox.addItems(STAT_GENERATION)
        self.abilityGeneration_comboBox.setFixedWidth(int(scale_factor * 110))
        customization_grid.addWidget(self.abilityGeneration_comboBox, 1, 0)

        advantage_label = HoverableLabel(FORCED_ADVANTAGE_TRANSLATE)
        customization_grid.addWidget(advantage_label, 0, 1)

        self.advantage_comboBox = HoverableComboBox()
        self.advantage_comboBox.addItems([DISABLED_M, NONE_M, ADVANTAGE_TRANSLATE, DISADVANTAGE_TRANSLATE])
        self.advantage_comboBox.setFixedWidth(int(scale_factor * 100))
        customization_grid.addWidget(self.advantage_comboBox, 1, 1)

        self.hitDie_label = HoverableLabel(HIT_DICE_TRANSLATE + " (1d8):")
        customization_grid.addWidget(self.hitDie_label, 0, 2)
        self.hitDie_label.hide()

        self.hitDie_lineEdit = QLineEdit("")
        customization_grid.addWidget(self.hitDie_lineEdit, 1, 2)
        self.hitDie_lineEdit.setFixedWidth(int(scale_factor * 100))
        self.hitDie_lineEdit.setValidator(integer_validator)
        self.hitDie_lineEdit.hide()

        self.hitDie_button = QPushButton(THROW_TRANSLATE, default=False, autoDefault=False)
        customization_grid.addWidget(self.hitDie_button, 2, 2)
        self.hitDie_button.setFixedWidth(int(scale_factor * 100))
        self.hitDie_button.hide()

        self.shortRest_button = QPushButton(SHORT_REST_TRANSLATE, default=False, autoDefault=False)
        customization_grid.addWidget(self.shortRest_button, 1, 3)
        self.shortRest_button.setFixedWidth(int(scale_factor * 100))
        self.shortRest_button.hide()

        self.longRest_button = QPushButton(LONG_REST_TRANSLATE, default=False, autoDefault=False)
        customization_grid.addWidget(self.longRest_button, 1, 4)
        self.longRest_button.setFixedWidth(int(scale_factor * 100))
        self.longRest_button.hide()

        self.stopRest_button = QPushButton(STOP_REST_TRANSLATE, default=False, autoDefault=False)
        customization_grid.addWidget(self.stopRest_button, 1, 5)
        self.stopRest_button.setFixedWidth(int(scale_factor * 100))
        self.stopRest_button.hide()

        self.languages_label = HoverableLabel(AVAILABLE_LANGUAGES)
        customization_grid.addWidget(self.languages_label, 0, 3)

        self.languages_comboBox = HoverableComboBox()
        self.languages_comboBox.addItems([ALL_TRANSLATE_F, STANDARD_TRANSLATE])
        self.languages_comboBox.setCurrentText(STANDARD_TRANSLATE)
        self.languages_comboBox.setFixedWidth(int(scale_factor * 100))
        customization_grid.addWidget(self.languages_comboBox, 1, 3)

        self.confirmFeats_button.pressed.connect(self.confirm_feats)

        self.cantrips_groupBox = QGroupBox(CANTRIPS)
        self.cantrips_scrollArea = QScrollArea()
        self.cantrips_scrollArea.setWidget(self.cantrips_groupBox)
        self.cantrips_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.cantrips_scrollArea, 1, 0, 2, 1)
        self.cantrips_vBox = QVBoxLayout()
        self.cantrips_groupBox.setLayout(self.cantrips_vBox)

        self.levelOne_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 1")
        self.levelOne_scrollArea = QScrollArea()
        self.levelOne_scrollArea.setWidget(self.levelOne_groupBox)
        self.levelOne_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelOne_scrollArea, 1, 1, 2, 1)
        self.levelOne_vBox = QVBoxLayout()
        self.levelOne_groupBox.setLayout(self.levelOne_vBox)

        self.levelTwo_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 2")
        self.levelTwo_scrollArea = QScrollArea()
        self.levelTwo_scrollArea.setWidget(self.levelTwo_groupBox)
        self.levelTwo_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelTwo_scrollArea, 1, 2)
        self.levelTwo_vBox = QVBoxLayout()
        self.levelTwo_groupBox.setLayout(self.levelTwo_vBox)

        self.levelThree_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 3")
        self.levelThree_scrollArea = QScrollArea()
        self.levelThree_scrollArea.setWidget(self.levelThree_groupBox)
        self.levelThree_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelThree_scrollArea, 1, 3)
        self.levelThree_vBox = QVBoxLayout()
        self.levelThree_groupBox.setLayout(self.levelThree_vBox)

        self.levelFour_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 4")
        self.levelFour_scrollArea = QScrollArea()
        self.levelFour_scrollArea.setWidget(self.levelFour_groupBox)
        self.levelFour_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelFour_scrollArea, 2, 2)
        self.levelFour_vBox = QVBoxLayout()
        self.levelFour_groupBox.setLayout(self.levelFour_vBox)

        self.levelFive_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 5")
        self.levelFive_scrollArea = QScrollArea()
        self.levelFive_scrollArea.setWidget(self.levelFive_groupBox)
        self.levelFive_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelFive_scrollArea, 2, 3)
        self.levelFive_vBox = QVBoxLayout()
        self.levelFive_groupBox.setLayout(self.levelFive_vBox)

        self.levelSix_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 6")
        self.levelSix_scrollArea = QScrollArea()
        self.levelSix_scrollArea.setWidget(self.levelSix_groupBox)
        self.levelSix_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelSix_scrollArea, 3, 0)
        self.levelSix_vBox = QVBoxLayout()
        self.levelSix_groupBox.setLayout(self.levelSix_vBox)

        self.levelSeven_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 7")
        self.levelSeven_scrollArea = QScrollArea()
        self.levelSeven_scrollArea.setWidget(self.levelSeven_groupBox)
        self.levelSeven_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelSeven_scrollArea, 3, 1)
        self.levelSeven_vBox = QVBoxLayout()
        self.levelSeven_groupBox.setLayout(self.levelSeven_vBox)

        self.levelEight_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 8")
        self.levelEight_scrollArea = QScrollArea()
        self.levelEight_scrollArea.setWidget(self.levelEight_groupBox)
        self.levelEight_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelEight_scrollArea, 3, 2)
        self.levelEight_vBox = QVBoxLayout()
        self.levelEight_groupBox.setLayout(self.levelEight_vBox)

        self.levelNine_groupBox = QGroupBox(f"{LEVEL_TRANSLATE} 9")
        self.levelNine_scrollArea = QScrollArea()
        self.levelNine_scrollArea.setWidget(self.levelNine_groupBox)
        self.levelNine_scrollArea.setWidgetResizable(True)
        magic_grid.addWidget(self.levelNine_scrollArea, 3, 3)
        self.levelNine_vBox = QVBoxLayout()
        self.levelNine_groupBox.setLayout(self.levelNine_vBox)

        conditions_groupBox = QGroupBox(CONDITIONS_TRANSLATE)
        status_grid.addWidget(conditions_groupBox, 0, 0)
        conditions_groupBox.setFixedHeight(int(scale_factor * 600))
        conditions_groupBox.setFixedWidth(int(scale_factor * 200))
        conditions_vBox = QVBoxLayout()
        conditions_groupBox.setLayout(conditions_vBox)

        self.blinded_checkBox = QCheckBox(BLINDED)
        conditions_vBox.addWidget(self.blinded_checkBox)

        self.charmed_checkBox = QCheckBox(CHARMED)
        conditions_vBox.addWidget(self.charmed_checkBox)

        self.deafened_checkBox = QCheckBox(DEAFENED)
        conditions_vBox.addWidget(self.deafened_checkBox)

        exhaustion_label = HoverableLabel(f"{EXHAUSTION}:")
        exhaustion_label.setFixedHeight(int(scale_factor * 20))
        conditions_vBox.addWidget(exhaustion_label)
        self.exhaustion_comboBox = HoverableComboBox()
        self.exhaustion_comboBox.addItems([str(i) for i in range(7)])
        conditions_vBox.addWidget(self.exhaustion_comboBox)
        self.exhaustion_comboBox.setFixedWidth(int(scale_factor * 40))

        self.frightened_checkBox = QCheckBox(FRIGHTENED)
        conditions_vBox.addWidget(self.frightened_checkBox)

        self.grappled_checkBox = QCheckBox(GRAPPLED)
        conditions_vBox.addWidget(self.grappled_checkBox)

        self.incapacitated_checkBox = QCheckBox(INCAPACITATED)
        conditions_vBox.addWidget(self.incapacitated_checkBox)
        self.incapacitated_checkBox.setEnabled(False)

        self.invisible_checkBox = QCheckBox(INVISIBLE)
        conditions_vBox.addWidget(self.invisible_checkBox)

        self.paralyzed_checkBox = QCheckBox(PARALYZED)
        conditions_vBox.addWidget(self.paralyzed_checkBox)

        self.petrified_checkBox = QCheckBox(PETRIFIED)
        conditions_vBox.addWidget(self.petrified_checkBox)

        self.poisoned_checkBox = QCheckBox(POISONED)
        conditions_vBox.addWidget(self.poisoned_checkBox)

        self.prone_checkBox = QCheckBox(PRONE)
        conditions_vBox.addWidget(self.prone_checkBox)

        self.restrained_checkBox = QCheckBox(RESTRAINED)
        conditions_vBox.addWidget(self.restrained_checkBox)

        self.stunned_checkBox = QCheckBox(STUNNED)
        conditions_vBox.addWidget(self.stunned_checkBox) 

        self.unconscious_checkBox = QCheckBox(UNCONSCIOUS)
        conditions_vBox.addWidget(self.unconscious_checkBox)

        self.spell_vBoxes: List[QVBoxLayout] = [self.cantrips_vBox, self.levelOne_vBox, self.levelTwo_vBox, self.levelThree_vBox, self.levelFour_vBox, self.levelFive_vBox, self.levelSix_vBox, self.levelSeven_vBox, self.levelEight_vBox, self.levelNine_vBox]

        self.blinded_checkBox.clicked.connect(lambda: self.update_blinded(self.blinded_checkBox.isChecked()))
        self.charmed_checkBox.clicked.connect(lambda: self.update_charmed(self.charmed_checkBox.isChecked()))
        self.deafened_checkBox.clicked.connect(lambda: self.update_deafened(self.deafened_checkBox.isChecked()))
        self.frightened_checkBox.clicked.connect(lambda: self.update_frightened(self.frightened_checkBox.isChecked()))
        self.grappled_checkBox.clicked.connect(lambda: self.update_grappled(self.grappled_checkBox.isChecked()))
        self.incapacitated_checkBox.clicked.connect(lambda: self.update_incapacitated(self.incapacitated_checkBox.isChecked()))
        self.invisible_checkBox.clicked.connect(lambda: self.update_invisible(self.invisible_checkBox.isChecked()))
        self.paralyzed_checkBox.clicked.connect(lambda: self.update_paralyzed(self.paralyzed_checkBox.isChecked()))
        self.petrified_checkBox.clicked.connect(lambda: self.update_petrified(self.petrified_checkBox.isChecked()))
        self.poisoned_checkBox.clicked.connect(lambda: self.update_poisoned(self.poisoned_checkBox.isChecked()))
        self.prone_checkBox.clicked.connect(lambda: self.update_prone(self.prone_checkBox.isChecked()))
        self.restrained_checkBox.clicked.connect(lambda: self.update_restrained(self.restrained_checkBox.isChecked()))
        self.stunned_checkBox.clicked.connect(lambda: self.update_stunned(self.stunned_checkBox.isChecked()))
        self.unconscious_checkBox.clicked.connect(lambda: self.update_unconscious(self.unconscious_checkBox.isChecked()))

        self.abilityGeneration_comboBox.activated.connect(lambda: self.update_ability_generation(self.abilityGeneration_comboBox.currentText()))
        self.alignment_comboBox.activated.connect(lambda: self.update_alignment(self.alignment_comboBox.currentText()))
        self.gender_comboBox.activated.connect(lambda: self.update_gender(self.gender_comboBox.currentText()))
        self.species_comboBox.activated.connect(lambda: self.update_species(self.species_comboBox.currentText()))
        self.lineage_comboBox.activated.connect(lambda: self.update_lineage(self.lineage_comboBox.currentText()))
        self.class_comboBox.activated.connect(lambda: self.update_class(self.class_comboBox.currentText()))
        self.subclass_comboBox.activated.connect(lambda: self.update_subclass(self.subclass_comboBox.currentText()))
        self.level_comboBox.activated.connect(lambda: self.update_level(self.level_comboBox.currentText()))
        self.background_comboBox.activated.connect(lambda: self.update_background(self.background_comboBox.currentText()))
        self.advantage_comboBox.activated.connect(lambda: self.update_advantage(self.advantage_comboBox.currentIndex()))
        self.languages_comboBox.activated.connect(lambda: self.update_available_languages(self.languages_comboBox.currentText()))
        self.finesse_comboBox.activated.connect(lambda: self.update_finesse(self.finesse_comboBox.currentText()))
        self.exhaustion_comboBox.activated.connect(lambda: self.update_exhaustion(self.exhaustion_comboBox.currentIndex()))

        self.equipmentFixed_checkBox.clicked.connect(lambda: self.update_equipment_fixed(self.equipmentFixed_checkBox.isChecked()))
        self.health_checkBox.clicked.connect(lambda: self.update_health_fixed(self.health_checkBox.isChecked()))
        self.shortRest_button.clicked.connect(self.short_rest)
        self.longRest_button.clicked.connect(self.long_rest)
        self.stopRest_button.clicked.connect(self.stop_rest)
        self.hitDie_button.clicked.connect(self.hit_die)

        self.name_lineEdit.editingFinished.connect(lambda: self.update_name(self.name_lineEdit.text()))
        self.ageMax_lineEdit.editingFinished.connect(lambda: self.update_ageMax(self.ageMax_lineEdit.text()))
        self.ageMin_lineEdit.editingFinished.connect(lambda: self.update_ageMin(self.ageMin_lineEdit.text()))
        self.height_lineEdit.editingFinished.connect(lambda: self.update_height(self.height_lineEdit.text()))
        self.weight_lineEdit.editingFinished.connect(lambda: self.update_weight(self.weight_lineEdit.text()))
        self.age_lineEdit.editingFinished.connect(lambda: self.update_age(self.age_lineEdit.text()))
        self.health_lineEdit.editingFinished.connect(lambda: self.update_health(self.health_lineEdit.text()))
        self.healthCurrent_lineEdit.editingFinished.connect(lambda: self.update_health_current(self.healthCurrent_lineEdit.text()))
        self.hitDie_lineEdit.editingFinished.connect(lambda: self.update_hit_die(self.hitDie_lineEdit.text()))
        self.copper_lineEdit.editingFinished.connect(lambda: self.update_copper(self.copper_lineEdit.text()))
        self.silver_lineEdit.editingFinished.connect(lambda: self.update_silver(self.silver_lineEdit.text()))
        self.electrum_lineEdit.editingFinished.connect(lambda: self.update_electrum(self.electrum_lineEdit.text()))
        self.gold_lineEdit.editingFinished.connect(lambda: self.update_gold(self.gold_lineEdit.text()))
        self.platinum_lineEdit.editingFinished.connect(lambda: self.update_platinum(self.platinum_lineEdit.text()))

        self.name_lineEdit.textEdited.connect(lambda: validate_input(self.name_lineEdit))
        self.ageMax_lineEdit.textEdited.connect(lambda: validate_input(self.ageMax_lineEdit))
        self.ageMin_lineEdit.textEdited.connect(lambda: validate_input(self.ageMin_lineEdit))
        self.height_lineEdit.textEdited.connect(lambda: validate_input(self.height_lineEdit))
        self.weight_lineEdit.textEdited.connect(lambda: validate_input(self.weight_lineEdit))
        self.age_lineEdit.textEdited.connect(lambda: validate_input(self.age_lineEdit))
        self.health_lineEdit.textEdited.connect(lambda: validate_input(self.health_lineEdit))
        self.healthCurrent_lineEdit.textEdited.connect(lambda: validate_input(self.healthCurrent_lineEdit))
        self.hitDie_lineEdit.textEdited.connect(lambda: validate_input(self.hitDie_lineEdit))
        self.copper_lineEdit.textEdited.connect(lambda: validate_input(self.copper_lineEdit))
        self.silver_lineEdit.textEdited.connect(lambda: validate_input(self.silver_lineEdit))
        self.electrum_lineEdit.textEdited.connect(lambda: validate_input(self.electrum_lineEdit))
        self.gold_lineEdit.textEdited.connect(lambda: validate_input(self.gold_lineEdit))
        self.platinum_lineEdit.textEdited.connect(lambda: validate_input(self.platinum_lineEdit))

        # Defining which elements whill need to appear/disappear later on
        # DisableEnable means that the element will be disabled when the window is in show mode, and enabled when it is in edit mode
        self.update_disable_enable() # For this one I create a function, as its elements whill need to be updated later on
        
        self.enableDisable = (
            [self.hitDie_lineEdit,
             self.hitDie_button]
        )

        self.hideShow = ( 
            [i["base_lineEdit"] for i in self.abilities_widgets.values()] +
            [i["base_label"] for i in self.abilities_widgets.values()] +
            [i["base_comboBox"] for i in self.abilities_widgets.values()] +
            [i["basePoint_label"] for i in self.abilities_widgets.values()] +
            [i["minus_button"] for i in self.abilities_widgets.values()] +
            [i["plus_button"] for i in self.abilities_widgets.values()] +
            [self.finish_button,
            self.ageMin_label,
            self.ageMin_lineEdit,
            self.ageMax_label,
            self.ageMax_lineEdit,
            self.health_checkBox,
            self.abilityGeneration_comboBox,
            self.abilityGeneration_label,
            self.languages_comboBox,
            self.languages_label]
        )

        self.showHide = (
            [self.edit_button,
            self.healthCurrent_lineEdit,
            self.levelUp_button,
            self.save_button,
            self.saveAs_button,
            self.equipment_lineEdit,
            self.shortRest_button,
            self.longRest_button]
        )

        space = 0 # This value changes if there is an image available to make room for it

        # Here is where the different types of window differentiate. The character generator and the character sheet
        if generator: 
            self.add_equipment(UNARMED_STRIKE, False, 0, 0, 0)
            self.add_equipment(IMPROVISED_WEAPON, False, 0, 0, 0)

            self.picture = None

            number_lineEdit = QLineEdit()
            number_lineEdit.setPlaceholderText(SHEET_NUMBER_TRANSLATE)
            number_lineEdit.setFixedWidth(int(scale_factor * 100))
            system_grid.addWidget(number_lineEdit, 0, 1)
            number_lineEdit.setValidator(number_validator)
            number_lineEdit.textEdited.connect(lambda: validate_input(number_lineEdit))

            self.load_button = QPushButton(LOAD_TRANSLATE, default=False, autoDefault=False)
            system_grid.addWidget(self.load_button, 1, 0)
            self.load_button.clicked.connect(self.load)

            self.confirm_button.clicked.connect(lambda: self.confirm(number_lineEdit.text()))

            self.update_abilities()

        else:
            self.fill()

            for ability in ABILITIES:
                self.abilities_widgets[ability]["final_label"].setStyleSheet("color: white;")

            if self.picture:
                picture_label = HoverableLabel()
                pixmap = QPixmap(self.picture)
                picture_label.setPixmap(pixmap.scaledToHeight(300))
                info_grid.addWidget(picture_label, 1, 1, 1, 2)
                space = 1

            self.save_button.clicked.connect(self.save)
            self.finish_button.clicked.connect(self.finish)
            self.saveAs_button.clicked.connect(self.save_as)
            self.edit_button.clicked.connect(self.edit)

            self.confirm_button.hide()
            
            self.toggle(self.hideShow, self.showHide, self.disableEnable, self.enableDisable)

            if self.character_info["level"] == "0" or self.character_info["level"] == "20":
                self.levelUp_button.hide()

        dice_groupBox = QGroupBox(DIE_TRANSLATE)
        dice_grid = QGridLayout()
        dice_groupBox.setLayout(dice_grid)
        result_label = HoverableLabel(f"{RESULT_TRANSLATE}:")
        self.result_button = QPushButton()
        self.advantage_label = HoverableLabel()
        self.results_label = HoverableLabel()
        dice_grid.addWidget(result_label, 0, 0)
        dice_grid.addWidget(self.result_button, 0, 1)
        dice_grid.addWidget(self.advantage_label, 1, 0)
        dice_grid.addWidget(self.results_label, 2, 0)

        info_grid.addWidget(abilities_groupBox, 0, 0, 3 + space, 1)
        info_grid.addWidget(health_groupBox, 0, 1, 1, 1)
        info_grid.addWidget(dice_groupBox, 1, 1, 1, 1)
        info_grid.addWidget(parameters_groupBox, 0, 2, 2, 1)
        info_grid.addWidget(class_scrollArea, 2 + space, 1, 2, 2)
        info_grid.addWidget(system_groupBox, 3 + space, 0)
        info_grid.addWidget(self.proficiencies_scrollArea, 0, 3, 4 + space, 1)
        info_grid.addWidget(self.customization_groupBox, 4 + space, 0, 1, 4)

        extra_grid.addWidget(looks_groupBox, 0, 0)


    def add_equipment(self, element, lock = False, xx = 0, yy = 0, offset = 2): # Offset is needed to have space for bare hand/improvised weapon
        if isinstance (element, list):
            if self.equipment_widgets:
                for index, widgets in enumerate(self.equipment_widgets):
                    if not widgets:
                        break
                index += 1
            else:
                index = 0

            comboBox = HoverableComboBox()
            comboBox.addItems(element)
            if element[1] in EQUIPMENTS_BASE_CLASS or EQUIPMENTS_BASE_BACKGROUND:
                comboBox.setCurrentIndex(1)

            comboBox.activated.connect(lambda: self.choose_optional_equipment(index, comboBox.currentText(), xx, yy))
            space1 = QLabel("")
            space1.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(space1, index + offset, 0)

            space2 = QLabel("")
            space2.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(space2, index + offset, 1)

            space3 = QLabel("")
            space3.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(space3, index + offset, 2)

            comboBox.setFixedWidth(int(20 + scale_factor * 175))
            self.equipment_grid.addWidget(comboBox, index + offset, 3)

            space4 = QLabel("")
            space4.setFixedWidth(int(scale_factor * 50))
            self.equipment_grid.addWidget(space4, index + offset, 4)

            space5 = QLabel("")
            space5.setFixedWidth(int(scale_factor * 50))
            self.equipment_grid.addWidget(space5, index + offset, 5)

            space6 = QLabel("")
            space6.setFixedWidth(int(scale_factor * 100))
            self.equipment_grid.addWidget(space6, index + offset, 6)

            widgets = {
                "checkBox1": space1,
                "checkBox2": space2,
                "label": space3,
                "comboBox": comboBox,
                "button0": space4,
                "button1": space5,
                "button2": space6,
                "xx": xx,
                "yy": yy
            }
            if index >= len(self.equipment_widgets):
                self.equipment_widgets.append(widgets)
            else:
                self.equipment_widgets[index] = widgets

                
        elif isinstance (element, str) and element.split(" ")[0].isdigit():
            if element.split(" ")[1] == COPPER:
                self.copper_lineEdit.setText(str(int(self.copper_lineEdit.text()) + int(element.split(" ")[0])))
                self.update_copper(self.copper_lineEdit.text())
                self.addedCopper += int(element.split(" ")[0])
            elif element.split(" ")[1] == SILVER:
                self.silver_lineEdit.setText(str(int(self.silver_lineEdit.text()) + int(element.split(" ")[0])))
                self.update_silver(self.silver_lineEdit.text())
                self.addedSilver += int(element.split(" ")[0])
            elif element.split(" ")[1] == ELECTRUM:
                self.electrum_lineEdit.setText(str(int(self.electrum_lineEdit.text()) + int(element.split(" ")[0])))
                self.update_electrum(self.electrum_lineEdit.text())
                self.addedElectrum += int(element.split(" ")[0])
            elif element.split(" ")[1] == GOLD:
                self.gold_lineEdit.setText(str(int(self.gold_lineEdit.text()) + int(element.split(" ")[0])))
                self.update_gold(self.gold_lineEdit.text())
                self.addedGold += int(element.split(" ")[0])
            elif element.split(" ")[1] == PLATINUM:
                self.platinum_lineEdit.setText(str(int(self.platinum_lineEdit.text()) + int(element.split(" ")[0])))
                self.update_platinum(self.platinum_lineEdit.text())
                self.addedPlatinum += int(element.split(" ")[0])

        elif element not in self.character_info["equipment"]:
            if self.equipment_widgets:
                for index, widgets in enumerate(self.equipment_widgets):
                    if not widgets:
                        index -= 1
                        break
                index += 1
            else:
                index = 0

            label = HoverableLabel(element)

            if element != UNARMED_STRIKE and element != IMPROVISED_WEAPON:
                lineEdit = QLineEdit("1")
                lineEdit.editingFinished.connect(lambda: validate_input(lineEdit, "1"))
                lineEdit.editingFinished.connect(lambda: self.update_equipment_quantity(element, lineEdit.text()))
                lineEdit.setValidator(items_validator)
            else:
                lineEdit = QLabel("")

            if element in WEAPONS:
                checkBox1 = QCheckBox()
                checkBox1.clicked.connect(lambda: self.update_off_hand(element, checkBox1.isChecked()))
                checkBox1.setEnabled(False)

                if not LIGHT in WEAPONS[element]["properties"]:
                    checkBox1.hide()

                checkBox2 = QCheckBox()
                checkBox2.clicked.connect(lambda: self.update_main_hand(element, checkBox2.isChecked()))

                button0 = QPushButton(HIT, default=False, autoDefault=False)
                button0.clicked.connect(lambda _, b = element: self.dice_weapon(b))

                button1 = QPushButton(DAMAGE, default=False, autoDefault=False)
                button1.clicked.connect(lambda _, b = element, c = button0: self.dice_weapon(b, c, 1))
                
                button2 = QPushButton(f"{DAMAGE} {EXTRA_TRANSLATE}", default=False, autoDefault=False)
                button2.clicked.connect(lambda _, b = element, c = button0: self.dice_weapon(b, c, 2)) 

            elif element in ARMORS:
                if element in WEAPON_TYPES[SHIELDS_TRANSLATE]:
                    checkBox1 = QCheckBox()
                    checkBox1.clicked.connect(lambda: self.update_armor(element, checkBox1.isChecked(), True))
                    checkBox2 = QLabel("")
                else:
                    checkBox1 = QLabel("")
                    checkBox2 = QCheckBox()
                    checkBox2.clicked.connect(lambda: self.update_armor(element, checkBox2.isChecked()))
                button0 = QLabel("")
                button1 = QLabel("")
                button2 = QLabel("")

            elif element in ADVENTURING_GEAR and "uses" in ADVENTURING_GEAR[element]:
                checkBox1 = QLabel("")

                checkBox2 = QCheckBox()
                checkBox2.clicked.connect(lambda: self.update_main_hand(element, checkBox2.isChecked()))

                lineEdit.setText(str(ADVENTURING_GEAR[element]["uses"]))

                button0 = QPushButton(USE, default=False, autoDefault=False)
                button0.clicked.connect(lambda _, a = element: self.update_equipment_quantity(a, self.character_info["equipment"][a] - 1)),

                button1 = QLabel("")
                button2 = QLabel("")

            else:
                checkBox1 = QLabel("")
                checkBox2 = QLabel("")
                button0 = QLabel("")
                button1 = QLabel("")
                button2 = QLabel("")

            space00 = QLabel("")    
            space0 = QLabel("")
            space1 = QLabel("")
            space2 = QLabel("")

            space00.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(space00, index + offset, 0)

            checkBox1.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(checkBox1, index + offset, 0)
            checkBox2.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(checkBox2, index + offset, 1)
            
            lineEdit.setFixedWidth(int(scale_factor * 30))
            self.equipment_grid.addWidget(lineEdit, index + offset, 2)
            label.setFixedWidth(int(scale_factor * 175))
            self.equipment_grid.addWidget(label, index + offset, 3)
            space0.setFixedWidth(int(scale_factor * 50))
            self.equipment_grid.addWidget(space0, index + offset, 4)
            space1.setFixedWidth(int(scale_factor * 50))
            self.equipment_grid.addWidget(space1, index + offset, 5)
            space2.setFixedWidth(int(scale_factor * 100))
            self.equipment_grid.addWidget(space2, index + offset, 6)

            button0.setFixedWidth(int(scale_factor * 50))
            self.equipment_grid.addWidget(button0, index + offset, 4)
            button0.hide()
            button1.setFixedWidth(int(scale_factor * 50))
            self.equipment_grid.addWidget(button1, index + offset, 5)
            button1.hide()
            button2.setFixedWidth(int(scale_factor * 100))
            self.equipment_grid.addWidget(button2, index + offset, 6)
            button2.hide()

            widgets = {
                "checkBox1": checkBox1,
                "checkBox2": checkBox2,
                "lineEdit": lineEdit,
                "label": label,
                "button0": button0,
                "button1": button1,
                "button2": button2,
                "space00": space00,
                "space0 ": space0,
                "space1": space1,
                "space2": space2
            }
            if index >= len(self.equipment_widgets):
                self.equipment_widgets.append(widgets)
            else:
                self.equipment_widgets[index] = widgets
            
            if lineEdit.text():
                self.character_info["equipment"] = {**self.character_info["equipment"], element: int(lineEdit.text())}
            else:
                self.character_info["equipment"] = {**self.character_info["equipment"], element: 1}
        elif element in ADVENTURING_GEAR and "uses" in ADVENTURING_GEAR[element]:
            self.update_equipment_quantity(element, self.character_info["equipment"][element] + ADVENTURING_GEAR[element]["uses"])
        else:
            self.update_equipment_quantity(element, self.character_info["equipment"][element] + 1)

        if lock: # In case the equipment needs to be locked (the used applied some edits)
            self.equipment_lineEdit.clear()

            # Use QTimer to delay the scroll operation (10 ms should be enough)
            QTimer.singleShot(10, lambda: self.proficiencies_scrollArea.verticalScrollBar().setValue(self.proficiencies_scrollArea.verticalScrollBar().maximum()))
            
            self.equipmentFixed_checkBox.setChecked(True)
            self.character_info["equipmentFixed"] = True

        for i in range(LEN_FEATURES):
            self.update_features(i, False)


    def add_optional_equipment(self, index: int, element):
        if element != RANDOM:
            for widget in self.equipment_widgets[index].values():
                if isinstance(widget, QWidget):
                    widget.hide()
                    widget.deleteLater()
            
            if isinstance(element, str) and len (element.split("MULTIPLE ")) > 1:
                self.add_equipment(element.split("MULTIPLE ")[-1])
                self.update_equipment_quantity(element.split("MULTIPLE ")[-1], element.split("MULTIPLE ")[0])
            elif element in EQUIPMENTS_BASE_CLASS: 
                yy = 0
                for item in CLASSES[EQUIPMENTS_BASE_CLASS[element]]["equip"]:
                    if isinstance(item, str) and len (item.split("MULTIPLE ")) > 1:
                        self.add_equipment(item.split("MULTIPLE ")[-1])
                        self.update_equipment_quantity(item.split("MULTIPLE ")[-1], item.split("MULTIPLE ")[0])
                    elif isinstance(item, str):
                        self.add_equipment(item)
                    else:
                        yy += 1
                        self.add_equipment(item, False, 0, yy)
            elif element in EQUIPMENTS_BASE_BACKGROUND: 
                yy = 0
                for item in BACKGROUNDS[EQUIPMENTS_BASE_BACKGROUND[element]]["equip"]:
                    if isinstance(item, str) and len (item.split("MULTIPLE ")) > 1:
                        self.add_equipment(item.split("MULTIPLE ")[-1])
                        self.update_equipment_quantity(item.split("MULTIPLE ")[-1], item.split("MULTIPLE ")[0])
                    elif isinstance(item, str):
                        self.add_equipment(item)
                    else:
                        yy += 1
                        self.add_equipment(item, False, 1, yy)
            else:
                self.add_equipment(element)

            self.equipment_widgets[index] = {}


    def change(self): # Should be called at the end of each change. May be called more than once, I may think of a solution to this
        if mainWindow.tabs.currentIndex(): # Edit name of tab unless it's the generator tab
            mainWindow.tabs.setTabText(mainWindow.tabs.currentIndex(), self.character_info["savename"] + "*")
            self.saved = False


    def choose_optional_equipment(self, index, element, xx: int, yy): # I use xx and yy to nest choices. A choice in equipment might have subsequent choices (i.e. a class equipment might have a choice between various weapons)
        if element:
            if len(self.character_info["equipmentBase"][xx]) > yy:
                self.character_info["equipmentBase"][xx][yy] = element
            else:
                self.character_info["equipmentBase"][xx].append(element)
            if not yy:
                for y in range(1, len(self.character_info["equipmentBase"][xx])):
                    self.character_info["equipmentBase"][xx][y] = None
        else:
            for y in range(len(self.character_info["equipmentBase"][xx])):
                self.character_info["equipmentBase"][xx][y] = None
        if not yy:
            for comboBox in self.equipmentChoice_comboBoxes[xx]:
                comboBox.hide()
                comboBox.deleteLater()
            self.equipmentChoice_comboBoxes[xx] = []
            if element in EQUIPMENTS_BASE_CLASS:
                for item in CLASSES[EQUIPMENTS_BASE_CLASS[element]]["equip"]:
                    if isinstance(item, list):
                        yy += 1
                        comboBox = HoverableComboBox()
                        comboBox.addItems(item)
                        comboBox.activated.connect(lambda: self.choose_optional_equipment(index, comboBox.currentText(), xx, yy))
                        comboBox.setFixedWidth(int(scale_factor * 215))
                        self.equipment_grid.addWidget(comboBox, index, 1 + 3 * yy, 1, 3)
                        self.equipmentChoice_comboBoxes[xx].append(comboBox)
            elif element in EQUIPMENTS_BASE_BACKGROUND:
                for item in BACKGROUNDS[EQUIPMENTS_BASE_BACKGROUND[element]]["equip"]:
                    if isinstance(item, list):
                        yy += 1
                        comboBox = HoverableComboBox()
                        comboBox.addItems(item)
                        comboBox.activated.connect(lambda: self.choose_optional_equipment(index, comboBox.currentText(), xx, yy))
                        comboBox.setFixedWidth(int(scale_factor * 215))
                        self.equipment_grid.addWidget(comboBox, index, 1 + 3 * yy, 1, 3)
                        self.equipmentChoice_comboBoxes[xx].append(comboBox)
    

    def confirm(self, number):
        if not number:
            number = 1
        else:
            number = int(number)

        for _ in range(number):
            self.gather_info()
            new_tab = CharacterTab(False, self.character_info)
            mainWindow.tabs.addTab(new_tab, new_tab.name_lineEdit.text() + "*")

        mainWindow.tabs.setCurrentWidget(new_tab)


    def confirm_feats(self):
        for i in range(2, LEN_FEATS):
            for ability in ABILITIES:
                self.character_info[ability] = self.abilities_widgets[ability]["final_label"].text()
            
            self.feats_widgets[i]["comboBox"].setEditable(True)
            self.feats_widgets[i]["comboBox"].lineEdit().setReadOnly(True)
            self.feats_widgets[i]["comboBox"].lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
            self.feats_widgets[i]["comboBox"].disableWheel()
            font = self.feats_widgets[i]["comboBox"].lineEdit().font()
            font.setPointSize(int(12 * scale_factor))
            self.feats_widgets[i]["comboBox"].lineEdit().setFont(font)
            self.feats_widgets[i]["comboBox1"].setEditable(True)
            self.feats_widgets[i]["comboBox1"].lineEdit().setReadOnly(True)
            self.feats_widgets[i]["comboBox1"].lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
            self.feats_widgets[i]["comboBox1"].disableWheel()
            font = self.feats_widgets[i]["comboBox1"].lineEdit().font()
            font.setPointSize(int(12 * scale_factor))
            self.feats_widgets[i]["comboBox1"].lineEdit().setFont(font)
            self.feats_widgets[i]["comboBox2"].setEditable(True)
            self.feats_widgets[i]["comboBox2"].lineEdit().setReadOnly(True)
            self.feats_widgets[i]["comboBox2"].lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
            self.feats_widgets[i]["comboBox2"].disableWheel()
            font = self.feats_widgets[i]["comboBox2"].lineEdit().font()
            font.setPointSize(int(12 * scale_factor))
            self.feats_widgets[i]["comboBox2"].lineEdit().setFont(font)

        for i in range(LEN_FEATS):
            if self.character_info["feats"][i] == RANDOM:
                possibilities = [self.feats_widgets[i]["comboBox"].itemText(j) for j in range(1, self.feats_widgets[i]["comboBox"].count())]
                random.shuffle(possibilities)
                self.character_info["feats"][i] = possibilities[0]
            self.feats_widgets[i]["comboBox"].setCurrentText(self.character_info["feats"][i])

            self.update_feat(i)

            if self.character_info["feats1"][i] == RANDOM and not self.feats_widgets[i]["comboBox"].isHidden():
                self.character_info["feats1"][i] = random.choice(ABILITIES)
            if self.character_info["feats2"][i] == RANDOM and not self.feats_widgets[i]["comboBox"].isHidden():
                self.character_info["feats2"][i] = random.choice(ABILITIES)

            self.feats_widgets[i]["comboBox1"].setCurrentText(self.character_info["feats1"][i])
            self.feats_widgets[i]["comboBox2"].setCurrentText(self.character_info["feats2"][i])

            self.update_feat(i)

        self.choosingFeat = False
        self.update_ability_bonuses()
        self.confirmFeats_button.hide()


    def dice(self, bonus = 0, size_throws = "1d20", base = 0, criticalButton:QPushButton = None, advantage = False, disadvantage = False):
        size = int(size_throws.split("d")[-1])
        throws = int(size_throws.split("d")[0])
        button = self.result_button

        if criticalButton: # This happens when calculating the damage for a critical hit
            if "!" in criticalButton.text():
                throws = 2 * throws
                size_throws = f"{throws}d{size}"

        result, advdis, result1, result2 = self.throw(bonus, size_throws, base, advantage, disadvantage)

        if (result - bonus) == size * throws + base:
            button.setStyleSheet("""
                QPushButton {
                    color: black;
                    background-color: orange;
                    border: 1px solid #d3d3d3; 
                    border-radius: 5px;                                      
                }
            """)
            critical = "!"
        else:
            button.setStyleSheet("""
                QPushButton {
                    background-color: 2d2d2d;
                    border: 1px solid #d3d3d3; 
                    border-radius: 5px;                                      
                }
            """)
            critical = ""
        
        if advdis == 1:
            self.advantage_label.setText(ADVANTAGE_TRANSLATE)
            self.results_label.setText(f"{RESULTS_TRANSLATE}: {result1}|{result2}")
        elif advdis == 2:
            self.advantage_label.setText(DISADVANTAGE_TRANSLATE)
            self.results_label.setText(f"{RESULTS_TRANSLATE}: {result1}|{result2}")
        else:
            self.advantage_label.setText("")
            self.results_label.setText("")
        
        button.setText(f"{result}{critical}")


    def dice_weapon(self, weapon, critical = None, position = 0):
        if AMMUNITION in WEAPONS[weapon]["properties"]:
            base = self.ability_bonuses[1]
        elif FINESSE in WEAPONS[weapon]["properties"]:
            if not self.finesse_comboBox.currentIndex():
                base = max(self.ability_bonuses[0], self.ability_bonuses[1])
                if self.ability_bonuses[0] >= self.ability_bonuses[1] and self.raging and self.character_info["level"].isdigit():
                    base = base + RAGE_BONUS[int(self.character_info["level"])]
            elif self.finesse_comboBox.currentIndex() == 1:
                base = self.ability_bonuses[0]
                if self.raging and self.character_info["level"].isdigit():
                    base = base + RAGE_BONUS[int(self.character_info["level"])]
            else:
                base = self.ability_bonuses[1]
        elif weapon == IMPROVISED_WEAPON:
            base = 0
        else:
            base = self.ability_bonuses[0]
            if self.raging and self.character_info["level"].isdigit():
                base = base + RAGE_BONUS[int(self.character_info["level"])]

        if position == 2: # Extra attack (2nd hand) only applies bonus if negative
            base = min(base, 0)

        for weapon_proficiency in self.proficiencies:
            if (isinstance(weapon_proficiency, str) and weapon_proficiency in WEAPON_TYPES and weapon in WEAPON_TYPES[weapon_proficiency]) or weapon == UNARMED_STRIKE:
                base = base + self.proficiency
                break

        if position: # Damage roll
            self.dice(0, WEAPONS[weapon]["damage"], base, critical)
        else: # Hit roll
            advantage = False
            if self.character_info[BLINDED] or self.character_info[POISONED] or self.character_info[PRONE] or self.character_info[RESTRAINED]:
                disadvantage = True
            else:
                disadvantage = False
                    
            self.dice(0, "1d20", base, None, advantage, disadvantage)


    def disable_widgets(self, disable):
        for widget in disable:
            if isinstance(widget, QCheckBox):
                widget.setEnabled(False)
            elif isinstance(widget, HoverableComboBox):
                widget.setEditable(True)
                widget.lineEdit().setReadOnly(True)
                widget.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                widget.disableWheel()
                font = widget.lineEdit().font()
                font.setPointSize(int(12 * scale_factor))
                widget.lineEdit().setFont(font)
            elif isinstance(widget, QLineEdit):
                widget.setReadOnly(True)
                widget.setCursor(Qt.CursorShape.IBeamCursor)


    def edit(self):
        self.confirm_feats()
        self.toggle(self.showHide, self.hideShow, self.enableDisable, self.disableEnable)
        self.update_background(self.character_info["background"])
        for i, ability in enumerate(ABILITIES):
            self.abilities_widgets[ability]["base_lineEdit"].setText(str(self.abilitiesBase[i]))
        self.abilityGeneration_comboBox.setCurrentText(STAT_GENERATION[3])
        self.update_ability_generation(STAT_GENERATION[3])

    
    def enable_widgets(self, enable):
        for widget in enable:
            if isinstance(widget, QCheckBox):
                widget.setEnabled(True)
            elif isinstance(widget, HoverableComboBox):
                widget.setEditable(False)
                widget.enableWheel()
            elif isinstance(widget, QLineEdit):
                widget.setReadOnly(False)


    def fill(self):
        if self.character_info[BLINDED]:
            self.blinded_checkBox.setChecked(True)
            self.update_blinded(True)

        if self.character_info[CHARMED]:
            self.charmed_checkBox.setChecked(True)
            self.update_charmed(True)

        if self.character_info[DEAFENED]:
            self.deafened_checkBox.setChecked(True)
            self.update_deafened(True)

        if self.character_info[EXHAUSTION]:
            self.exhaustion_comboBox.setCurrentIndex(self.character_info[EXHAUSTION])
            self.update_exhaustion(self.character_info[EXHAUSTION])

        if self.character_info[FRIGHTENED]:
            self.frightened_checkBox.setChecked(True)
            self.update_frightened(True)

        if self.character_info[GRAPPLED]:
            self.grappled_checkBox.setChecked(True)
            self.update_grappled(True)

        if self.character_info[INVISIBLE]:
            self.invisible_checkBox.setChecked(True)
            self.update_invisible(True)

        if self.character_info[PARALYZED]:
            self.paralyzed_checkBox.setChecked(True)
            self.update_paralyzed(True)

        if self.character_info[PETRIFIED]:
            self.petrified_checkBox.setChecked(True)
            self.update_petrified(True)

        if self.character_info[POISONED]:
            self.poisoned_checkBox.setChecked(True)
            self.update_poisoned(True)

        if self.character_info[PRONE]:
            self.prone_checkBox.setChecked(True)
            self.update_prone(True)

        if self.character_info[RESTRAINED]:
            self.restrained_checkBox.setChecked(True)
            self.update_restrained(True)

        if self.character_info[STUNNED]:
            self.stunned_checkBox.setChecked(True)
            self.update_stunned(True)

        if self.character_info[UNCONSCIOUS]:
            self.unconscious_checkBox.setChecked(True)
            self.update_unconscious(True)

        if self.character_info["copper"]:
            self.copper_lineEdit.setText(str(int(self.copper_lineEdit.text()) + int(self.character_info["copper"])))

        if self.character_info["silver"]:
            self.silver_lineEdit.setText(str(int(self.silver_lineEdit.text()) + int(self.character_info["silver"])))

        if self.character_info["electrum"]:
            self.electrum_lineEdit.setText(str(int(self.electrum_lineEdit.text()) + int(self.character_info["electrum"])))

        if self.character_info["gold"]:
            self.gold_lineEdit.setText(str(int(self.gold_lineEdit.text()) + int(self.character_info["gold"])))

        if self.character_info["platinum"]:
            self.platinum_lineEdit.setText(str(int(self.platinum_lineEdit.text()) + int(self.character_info["platinum"])))
            
        languages = self.character_info["languages"]
        proficiencies = self.character_info["proficiencies"]

        self.finesse_comboBox.setCurrentText(self.character_info["finesse"])

        if self.character_info["species"] == RANDOM:
            self.character_info["species"] = random.choices(list(SPECIES_WEIGHTS), list(SPECIES_WEIGHTS.values()), k = 1)[0]
        self.species_comboBox.setCurrentText(self.character_info["species"])

        if self.character_info["lineage"] == RANDOM:
            self.character_info["lineage"] = random.choices(list(LINEAGE_WEIGHTS[self.character_info["species"]]), list(LINEAGE_WEIGHTS[self.character_info["species"]].values()), k = 1)[0]
        self.lineage_comboBox.addItem(self.character_info["lineage"])
        self.lineage_comboBox.setCurrentText(self.character_info["lineage"])

        if self.character_info["alignment"] == RANDOM and self.character_info["species"] in ALIGNMENT_WEIGHTS_1:
            self.character_info["alignment"] = f"{random.choices(ALIGNMENTS_1, ALIGNMENT_WEIGHTS_1[self.character_info["species"]], k = 1)[0]} {random.choices(ALIGNMENTS_2, ALIGNMENT_WEIGHTS_2[self.character_info["species"]], k = 1)[0]}"
        elif self.character_info["alignment"] == RANDOM and self.character_info["lineage"] in ALIGNMENT_WEIGHTS_1:
            self.character_info["alignment"] = f"{random.choices(ALIGNMENTS_1, ALIGNMENT_WEIGHTS_1[self.character_info["lineage"]], k = 1)[0]} {random.choices(ALIGNMENTS_2, ALIGNMENT_WEIGHTS_2[self.character_info["lineage"]], k = 1)[0]}"
        if self.character_info["alignment"] == f"{ALIGNMENTS_1[1]} {ALIGNMENTS_2[1]}":
            self.character_info["alignment"] = ALIGNMENTS[5]
        self.alignment_comboBox.setCurrentText(self.character_info["alignment"])

        if self.character_info["background"] == RANDOM:
            self.character_info["background"] = random.choices(list(BACKGROUND_WEIGHTS), list(BACKGROUND_WEIGHTS.values()), k = 1)[0]
        self.background_comboBox.setCurrentText(self.character_info["background"])

        if self.character_info["class"] == RANDOM:
            self.character_info["class"] = random.choices(list(CLASS_WEIGHTS), list(CLASS_WEIGHTS.values()), k = 1)[0]
        self.class_comboBox.setCurrentText(self.character_info["class"])

        if self.character_info["subclass"] == RANDOM:
            self.character_info["subclass"] = random.choices(list(SUBCLASS_WEIGHTS[self.character_info["class"]]), list(SUBCLASS_WEIGHTS[self.character_info["class"]].values()), k = 1)[0]
        self.subclass_comboBox.addItem(self.character_info["subclass"])
        self.subclass_comboBox.setCurrentText(self.character_info["subclass"])

        if self.character_info["level"] == RANDOM:
            if self.character_info["class"] != NONE_F:
                self.character_info["level"] = random.choices([str(i) for i in range(1, 21)], LEVELS_WEIGHTS, k = 1)[0]
            else:
                self.character_info["level"] = "0"
        self.level_comboBox.addItem(self.character_info["level"])
        self.level_comboBox.setCurrentText(self.character_info["level"])
        self.update_background(self.character_info["background"])

        for i in range(1, LEN_FEATS):
            feat = self.character_info["feats"][i]
            feat1 = self.character_info["feats1"][i]
            feat2 = self.character_info["feats2"][i]
            self.feats_widgets[i]["comboBox"].setCurrentText(feat)
            self.feats_widgets[i]["comboBox1"].setCurrentText(feat1)
            self.feats_widgets[i]["comboBox2"].setCurrentText(feat2)
            if feat:
                if "bonus ability 1" in FEATS[feat]:
                    if isinstance(FEATS[feat]["bonus ability 1"], list):
                        if feat1 and feat1 != RANDOM:
                            self.character_info[feat1] = str(int(self.character_info[feat1]) - 1)
                        if feat2 and feat2 != RANDOM:
                            self.character_info[feat2] = str(int(self.character_info[feat2]) - 1)
                    else:
                        self.character_info[FEATS[feat]["bonus ability 1"]] = str(int(self.character_info[FEATS[feat]["bonus ability 1"]]) - 1)
                elif "bonus ability 2" in FEATS[feat]:
                    if isinstance(FEATS[feat]["bonus ability 2"], list):
                        if feat1 and feat1 != RANDOM:
                            self.character_info[feat1] = str(int(self.character_info[feat1]) - 2)
                        if feat2 and feat2 != RANDOM:
                            self.character_info[feat2] = str(int(self.character_info[feat2]) - 2)
                    else:
                        self.character_info[FEATS[feat]["bonus ability 2"]] = str(int(self.character_info[FEATS[feat]["bonus ability 2"]]) - 2)

        self.update_level(self.character_info["level"])
        self.update_species(self.character_info["species"])
        
        for i in range(1, LEN_FEATS):
            if self.character_info["feats"][i] == RANDOM:
                if self.feats_widgets[i]["comboBox"].count():
                    possibilities = [self.feats_widgets[i]["comboBox"].itemText(j) for j in range(1, self.feats_widgets[i]["comboBox"].count())]
                    random.shuffle(possibilities)
                    self.character_info["feats"][i] = possibilities[0]
            self.feats_widgets[i]["comboBox"].setCurrentText(self.character_info["feats"][i])

            self.update_feat(i)

            if self.character_info["feats1"][i] == RANDOM and not self.feats_widgets[i]["comboBox"].isHidden():
                self.character_info["feats1"][i] = random.choice(ABILITIES)
            if self.character_info["feats2"][i] == RANDOM and not self.feats_widgets[i]["comboBox"].isHidden():
                self.character_info["feats2"][i] = random.choice(ABILITIES)

            self.feats_widgets[i]["comboBox1"].setCurrentText(self.character_info["feats1"][i])
            self.feats_widgets[i]["comboBox2"].setCurrentText(self.character_info["feats2"][i])

            self.update_feat(i)

        if self.character_info["gender"] == RANDOM:
            self.character_info["gender"] = random.choice([GENDERS[1], GENDERS[2]])
        self.gender_comboBox.setCurrentText(self.character_info["gender"])

        self.abilitiesBase = [0, 0, 0, 0, 0, 0]
        self.abilitiesFixed = [1, 1, 1, 1, 1, 1]
        for i, ability in enumerate(ABILITIES):
            self.abilitiesBase[i] = self.character_info[ability]

        for i, ability in enumerate(ABILITIES):
            self.abilitiesBase[i] = int(self.character_info[ability])
            if self.character_info[ability + "1"]:
                self.abilities_widgets[ability]["checkBox1"].setChecked(True)
                self.abilitiesBase[i] -= 1
            if self.character_info[ability + "2"]:
                self.abilities_widgets[ability]["checkBox2"].setChecked(True)
                self.abilitiesBase[i] -= 2

        self.update_background(self.character_info["background"])

        if self.character_info["ageMin"]:
            self.ageMin_lineEdit.setText(self.character_info["ageMin"])

        if self.character_info["ageMax"]:
            self.ageMax_lineEdit.setText(self.character_info["ageMax"])

        if not self.character_info["age"]:
            self.character_info["age"] = self.age_lineEdit.placeholderText()
        self.age_lineEdit.setText(self.character_info["age"])

        self.update_gender(self.character_info["gender"])

        if not self.character_info["height"]:
            self.character_info["height"] = self.height_lineEdit.placeholderText()
        self.height_lineEdit.setText(self.character_info["height"])

        if not self.character_info["weight"]:
            self.character_info["weight"] = self.weight_lineEdit.placeholderText()
        self.weight_lineEdit.setText(self.character_info["weight"])

        if not self.character_info["name"]:
            self.character_info["name"] = self.name_lineEdit.placeholderText()
        self.name_lineEdit.setText(self.character_info["name"])
        self.character_info["savename"] = self.character_info["name"]

        self.update_class(self.character_info["class"])

        bonus = [0, 1, 2, 3, 4, 5]
        randomise = [0, 1, 0, 1, 0, 1]
        bonusSkills = list(SKILLS.keys())

        for skill in SKILLS:
            if self.character_info[skill]:
                self.skills_widgets[skill]["checkBox"].setChecked(True)
                self.update_skills()

        random.shuffle(bonus)
        random.shuffle(randomise)
        random.shuffle(bonusSkills)

        for i in bonus:
            if randomise[i] and self.abilities_widgets[ABILITIES[i]]["checkBox2"].isEnabled() and not self.abilities_widgets[ABILITIES[i]]["checkBox2"].isChecked():
                self.abilities_widgets[ABILITIES[i]]["checkBox2"].setChecked(True)
                self.update_ability_bonus(ABILITIES[i], "2")
                self.character_info[ABILITIES[i] + "2"] = True
            elif self.abilities_widgets[ABILITIES[i]]["checkBox1"].isEnabled() and not self.abilities_widgets[ABILITIES[i]]["checkBox1"].isChecked():
                self.abilities_widgets[ABILITIES[i]]["checkBox1"].setChecked(True)
                self.update_ability_bonus(ABILITIES[i], "1")
                self.character_info[ABILITIES[i] + "1"] = True

        for skill in bonusSkills:
            if self.skills_widgets[skill]["checkBox"].isEnabled() and not self.skills_widgets[skill]["checkBox"].isChecked():
                self.skills_widgets[skill]["checkBox"].setChecked(True)
                self.update_skills(skill)
                self.character_info[skill] = True

        if not self.character_info["health"]:
            self.character_info["health"] = self.health_lineEdit.placeholderText()
            self.health_lineEdit.setText(self.character_info["health"])
        else:
            if "+" in self.character_info["health"]:
                healthTotal = 0
                for health in self.character_info["health"].split("+"):
                    if "d" in health:
                        result, _, _, _ = self.throw(0, health)
                        healthTotal += result
                self.character_info["health"] = str(healthTotal)
            elif "d" in self.character_info["health"]:
                result, _, _, _ = self.throw(0, self.character_info["health"])
                self.character_info["health"] = str(result)
            self.health_lineEdit.setText(self.character_info["health"])

        if not self.character_info["healthCurrent"] or "d" in self.character_info["healthCurrent"] or "+" in self.character_info["healthCurrent"]:
            self.character_info["healthCurrent"] = self.health_lineEdit.text()
        self.healthCurrent_lineEdit.setText(self.character_info["healthCurrent"])

        if self.character_info["healthFixed"]:
            self.health_checkBox.setChecked(True)
        
        j = 0
        if not any(lang for lang in languages):
            languages = self.character_info["languages"]
        for i, widget in enumerate(self.languages_widgets):
            if widget and isinstance(widget, HoverableComboBox):
                if len(languages) > j and languages[j] != RANDOM and languages[j] != RANDOM_STANDARD and languages[j] != RANDOM_RARE:
                    if languages[j] not in self.character_info["available languages"]:
                        widget.addItem(languages[j])
                    widget.setCurrentText(languages[j])
                else:
                    possibilities = [widget.itemText(j) for j in range(0, widget.count()) if widget.itemText(j) != RANDOM and widget.itemText(j) != RANDOM_STANDARD and widget.itemText(j) != RANDOM_RARE]
                    if widget.currentText() == RANDOM_STANDARD:
                        languages_weights = [1 if language not in RARE_LANGUAGES else 0 for language in possibilities]
                    elif widget.currentText() == RANDOM_RARE:
                        languages_weights = [0 if language not in RARE_LANGUAGES else 1 for language in possibilities]
                    else:
                        languages_weights = [10 if language not in RARE_LANGUAGES else 1 for language in possibilities]

                    language = random.choices(possibilities, languages_weights, k = 1)[0]
                    widget.setCurrentText(language)
                    if len(self.character_info["languages"][j]) > j:
                        self.character_info["languages"][j] = language
                    else:
                        self.character_info["languages"].append(language)
                j += 1
            self.update_languages(False)

        j = 0
        for i, widget in enumerate(self.proficiencies_widgets):
            if widget and isinstance(widget, HoverableComboBox):
                if len(proficiencies) > j and proficiencies[j] != RANDOM:
                    widget.setCurrentText(proficiencies[j])
                    self.character_info["proficiencies"][j] = proficiencies[j]
                else:
                    possibilities = [widget.itemText(j) for j in range(1, widget.count())]
                    random.shuffle(possibilities)
                    widget.setCurrentText(possibilities[0])
                    self.character_info["proficiencies"][j] = possibilities[0]
                j += 1

        if self.extra_comboBox:
            self.update_equipment_extra(self.extra_comboBox.currentText())

        if self.character_info["equipmentFixed"]:
            equipments = self.character_info["equipment"]

            self.character_info["equipment"] = {}

            for equipment in equipments:
                self.add_equipment(equipment, True)
                self.update_equipment_quantity(equipment, equipments[equipment])
        else:
            self.equipmentFixed_checkBox.setChecked(True)
            self.character_info["equipmentFixed"] = True

            for i, widgets in enumerate(self.equipment_widgets):
                if "comboBox" in widgets:
                    if widgets["comboBox"].currentText() == RANDOM:
                        if len(self.character_info["equipmentBase"][widgets["xx"]]) > widgets["yy"] and self.character_info["equipmentBase"][widgets["xx"]][widgets["yy"]] and self.character_info["equipmentBase"][widgets["xx"]][widgets["yy"]] != RANDOM:
                            widgets["comboBox"].setCurrentText(self.character_info["equipmentBase"][widgets["xx"]][widgets["yy"]])
                        else:
                            possibilities = [widgets["comboBox"].itemText(j) for j in range(1, widgets["comboBox"].count())]
                            random.shuffle(possibilities)
                            widgets["comboBox"].setCurrentText(possibilities[0])
                    self.add_optional_equipment(i, widgets["comboBox"].currentText())

        if self.character_info["hitDie"]:
            self.hitDie_lineEdit.setText(self.character_info["hitDie"])

        for i in range(LEN_FEATURES):
            self.update_features(i, False)

        self.stop_rest()

    
    def fill_point_buy(self, pointBuy):
        abilities = ABILITIES.copy()
        if pointBuy:
            for i, ability in enumerate (ABILITIES):
                value = int(self.abilities_widgets[ability]["basePoint_label"].text())
                while self.abilitiesChanged[i]:
                    if value > 13:
                        self.abilities_widgets[ability]["basePoint_label"].setText(str(value - 1))
                        value -= 1
                        self.points += 2
                    elif value > 8:
                        self.abilities_widgets[ability]["basePoint_label"].setText(str(value - 1))
                        value -= 1
                        self.points += 1
                    self.abilitiesChanged[i] -= 1
        else:
            while self.points:
                random.shuffle(abilities)
                for i, ability in enumerate (abilities):
                    value = int(self.abilities_widgets[ability]["basePoint_label"].text())
                    if value < 13 and self.points:
                        self.abilities_widgets[ability]["basePoint_label"].setText(str(value + 1))
                        self.points -= 1
                        self.abilitiesChanged[ABILITIES.index(ability)] += 1
                        break
                    elif value < 15 and self.points > 1:
                        self.abilities_widgets[ability]["basePoint_label"].setText(str(value + 1))
                        self.points -= 2
                        self.abilitiesChanged[ABILITIES.index(ability)] += 1
                        break


    def finish(self):
        self.gather_info()
        self.fill()
        self.toggle(self.hideShow, self.showHide, self.disableEnable, self.enableDisable)


    def gather_info(self):
        if not self.character_info["name"]:
            self.character_info["name"] = self.name_lineEdit.placeholderText()

        if not self.character_info["age"]:
            self.character_info["age"] = self.age_lineEdit.placeholderText()

        if not self.character_info["height"]:
            self.character_info["height"] = self.height_lineEdit.placeholderText()

        if not self.character_info["weight"]:
            self.character_info["weight"] = self.height_lineEdit.placeholderText()

        if not self.character_info["name"]:
            self.character_info["name"] = self.name_lineEdit.placeholderText()

        if not self.character_info["health"]:
            self.character_info["health"] = self.health_lineEdit.placeholderText()

        if self.abilityGeneration_comboBox.currentText() == STAT_GENERATION[1]:
            self.fill_point_buy(False)
            self.update_abilities()

        for ability in ABILITIES:
            self.character_info[ability] = self.abilities_widgets[ability]["final_label"].text()

        self.update_name_shown()
        self.update_heightWeight_shown()
        self.update_age_shown()
        self.update_name_shown()
        self.update_health_shown()

        if self.abilityGeneration_comboBox.currentText() == STAT_GENERATION[1]:
            self.fill_point_buy(True)
        self.update_ability_bonuses()


    def hit_die(self):
        hitDie = self.hitDie_lineEdit.text()

        if int(hitDie) and self.character_info["healthCurrent"]:
            hitDie = str(int(hitDie) - 1)
            self.hitDie_lineEdit.setText(hitDie)
            self.character_info["hitDie"] = hitDie

            self.dice(self.ability_bonuses[2], f"1d{self.hitDie}")
            self.healthCurrent_lineEdit.setText(str(int(self.character_info["hitDie"]) + int(self.character_info["healthCurrent"])))
            self.update_health_current(self.healthCurrent_lineEdit.text())


    def hide_widgets(self, hide: List[Union[QLineEdit, QLabel, QPushButton, QCheckBox, QComboBox]]):
        for i in hide:
            i.hide()
            

    def level_up(self):
        if int(self.character_info["level"]) and not self.choosingFeat:
            mainWindow.tabs.setTabText(mainWindow.tabs.currentIndex(), self.character_info["savename"] + "*")
            self.saved = False

            level = int(self.character_info["level"])
            if level < 20 and level > 0:
                health = int(self.character_info["health"])
                healthCurrent = int(self.character_info["healthCurrent"])
                bonus = 0

                if not self.character_info["healthFixed"]:
                    result, _, _, _ = self.throw(0, f"1d{self.hitDie}")
                    bonus += self.healthBonus + self.ability_bonuses[2] + result
                else:
                    bonus += self.healthBonus + self.ability_bonuses[2] + self.hitDieFixed

                self.health_lineEdit.setText(str(health + bonus))
                self.update_health(self.health_lineEdit.text())

                self.healthCurrent_lineEdit.setText(str(healthCurrent + bonus))
                self.update_health_current(self.healthCurrent_lineEdit.text())

                self.level_comboBox.setCurrentText(str(level + 1))

                for currentFeat in range(2, LEN_FEATS):
                    if self.feats_widgets[currentFeat]["comboBox"].isHidden():
                        break

                hitDie = int(self.hitDie_lineEdit.text()) + 1

                self.update_level(self.level_comboBox.currentText())

                self.hitDie_lineEdit.setText(str(hitDie))

                for newFeat in range(2, LEN_FEATS):
                    if self.feats_widgets[newFeat]["comboBox"].isHidden():
                        break

                if newFeat > currentFeat or not self.feats_widgets[currentFeat]["comboBox"].isHidden():
                    self.feats_widgets[currentFeat]["comboBox"].setEditable(False)
                    self.feats_widgets[currentFeat]["comboBox"].enableWheel()
                    self.choosingFeat = True
                    self.confirmFeats_button.show()

                if level + 1 == 20:
                    self.levelUp_button.hide()
                    mainWindow.setFocus()
            

    def load(self):
        file_path, _ = QFileDialog.getOpenFileName(self, LOAD_TEXT, BASE_PATH, TEXT_FILE_TRANSLATE + " (*.txt)")
        if file_path:
            with open(file_path) as f:
                character_info = ast.literal_eval(f.read())
                new_tab = CharacterTab(False, character_info)
                new_tab.savename = character_info["savename"]
                mainWindow.tabs.addTab(new_tab, character_info["savename"])
                new_tab.saved = True

            mainWindow.tabs.setCurrentWidget(new_tab)
            

    def long_rest(self):
        if self.character_info["healthCurrent"]:
            self.healthCurrent_lineEdit.setText(self.character_info["health"])
            self.update_health_current(self.character_info["health"])
            level = self.character_info["level"]
            if level != RANDOM and int(level):
                self.character_info["hitDie"] = self.character_info["level"]
                self.hitDie_lineEdit.setText(self.character_info["hitDie"])

        for i, widgets in enumerate(self.features_widgets):
            if widgets and (widgets[0] == LONG_REST_TRANSLATE or widgets[0] == "Long Rest after"):
                if widgets[0] == "Long Rest after": # If there are actions that need to be done immediatley after a long rest
                    self.stopRest_button.show()
                for widget in widgets[1:]:
                    if widget and isinstance(widget, HoverableComboBox) and widget.count() > 2:
                        widget.setEditable(False)
                        widget.enableWheel()
                        widget.setCurrentIndex(0)
                    elif widget and isinstance(widget, QPushButton):
                        if widget.text() != FINISH_TRANSLATE and widget.text() != CONFIRM_TRANSLATE:
                            widget.setText(THROW_TRANSLATE)
                            widget.setEnabled(True)
                            widget.setStyleSheet("""
                                QPushButton {
                                    background-color: 2d2d2d;
                                    border: 1px solid #d3d3d3; 
                                    border-radius: 5px;                                      
                                }
                            """)
                        elif widget.text() == FINISH_TRANSLATE:
                            widget.click()
            elif widgets and widgets[0] == "Long Rest PB":
                for widget in widgets[1:]:
                    if widget and isinstance(widget, QPushButton):
                        widget.setText(str(self.proficiency))
                        self.character_info["points"][i] = str(self.proficiency)
            elif widgets and widgets[0] == "Short Rest +1":
                for j, widget in enumerate(widgets[1:]):
                    if widget and isinstance(widget, int):
                        widgets[j].setText(str(widget))
                        self.character_info["points"][i] = str(widget)

        if self.longRest_remove: # If there are items that only last until a long rest
            for equip in self.longRest_remove:
                self.update_equipment_quantity(equip, 0)
            self.longRest_remove = []

        exhaustion = int(self.exhaustion_comboBox.currentText())
        if exhaustion:
            self.exhaustion_comboBox.setCurrentText(str(exhaustion - 1))
            self.update_exhaustion(exhaustion - 1)


    def minus_points(self, ability):
        value = self.abilitiesBase[ABILITIES.index(ability)]
        if value > 8 and value <  14:
            self.abilities_widgets[ability]["basePoint_label"].setText(str(value - 1))
            self.points += 1
            self.update_abilities()
        elif value >= 14:
            self.abilities_widgets[ability]["basePoint_label"].setText(str(value - 1))
            self.points += 2
            self.update_abilities()

    
    def plus_points(self, ability):
        value = self.abilitiesBase[ABILITIES.index(ability)]
        if value < 13 and self.points > 0:
            self.abilities_widgets[ability]["basePoint_label"].setText(str(value + 1))
            self.points -= 1
            self.update_abilities()
        elif value >= 13 and value < 15 and self.points > 1:
            self.abilities_widgets[ability]["basePoint_label"].setText(str(value + 1))
            self.points -= 2
            self.update_abilities()


    def save(self):
        self.confirm_feats()
        file_name = self.character_info["savename"]
        if self.savename:
            file_path = os.path.join(BASE_PATH, file_name + ".txt")
        else:
            file_path, _ = QFileDialog.getSaveFileName(self, SAVE_TEXT, os.path.join(BASE_PATH, file_name), TEXT_FILE_TRANSLATE + " (*.txt)")
        
        if file_path:
            self.savename = os.path.splitext(os.path.basename(file_path))[0]
            self.gather_info()
            self.saved = True
            mainWindow.tabs.setTabText(mainWindow.tabs.currentIndex(), self.savename)
            self.character_info["savename"] = self.savename
            with open(file_path, "w") as f:
                f.write(str(self.character_info))
            return True
        else:
            return False
        

    def save_as(self):
        self.confirm_feats()
        file_name = self.character_info["savename"]
        file_path, _ = QFileDialog.getSaveFileName(self, SAVE_TEXT, os.path.join(BASE_PATH, file_name), TEXT_FILE_TRANSLATE + " (*.txt)")

        if file_path:
            self.savename = os.path.splitext(os.path.basename(file_path))[0]
            self.gather_info()
            self.saved = True
            mainWindow.tabs.setTabText(mainWindow.tabs.currentIndex(), self.savename)
            self.character_info["savename"] = self.savename
            with open(file_path, "w") as f:
                f.write(str(self.character_info))
            return True
        else:
            return False # Shouldn't ever happen


    def short_rest(self):
        for i, widgets in enumerate(self.features_widgets):
            if widgets and widgets[0] == "Short Rest +1":
                for j, widget in enumerate(widgets[1:]):
                    if widget and isinstance(widget, int) and int(widgets[j].text())< widget:
                        widgets[j].setText(str(int(widgets[j].text()) + 1))
                        self.character_info["points"][i] = str(int(widgets[j].text()) + 1)


    def show_widgets(self, show: List[Union[QLineEdit, QLabel, QPushButton, QCheckBox, QComboBox]]):
        for i in show:
            i.show()


    def shuffle_abilities(self):
        # Calculating ability bonuses
        # Shuffling abilities
        # Extract elements to shuffle
        elements_to_shuffle = [self.abilitiesBase[i] for i in range(len(self.abilitiesBase)) if self.abilitiesFixed[i] == 0]

        # Shuffle the extracted elements
        random.shuffle(elements_to_shuffle)

        # Reinsert the shuffled elements
        j = 0
        for i in range(len(self.abilitiesBase)):
            if self.abilitiesFixed[i] == 0:
                self.abilitiesBase[i] = elements_to_shuffle[j]
                j += 1
        
        self.update_ability_bonuses()


    def stop_rest(self):
        for widgets in self.features_widgets:
            if widgets and widgets[0] == "Long Rest after":
                for widget in widgets[1:]:
                    if isinstance(widget, HoverableComboBox):
                        widget.setEditable(True)
                        widget.lineEdit().setReadOnly(True)
                        widget.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                        widget.disableWheel()
                        font = widget.lineEdit().font()
                        font.setPointSize(int(12 * scale_factor))
                        widget.lineEdit().setFont(font)
                    elif isinstance(widget, QPushButton):
                        widget.click()
        self.stopRest_button.hide()

    
    def throw(self, bonus = 0, size_throws = "1d20", base = 0, advantage = False, disadvantage = False):
        size = size_throws.split("d")[-1]
        throws = size_throws.split("d")[0]

        if throws:
            throws = int(throws)
        else:
            throws = 1

        if size:
            size = int(size)
        else:
            size = 20


        if self.advantage == 2:
            advantage = True
            disadvantage = False
        elif self.advantage == 3:
            advantage = False
            disadvantage = True
        elif self.advantage == 1 or (advantage and disadvantage):
            advantage = False
            disadvantage = False

        result = base
        result1 = base
        result2 = base
        for _ in range(throws):
            if not advantage and not disadvantage:  # No advantage or disadvantage
                roll_result = random.randint(1, size) + bonus
            else:  # Advantage or disadvantage
                roll1 = random.randint(1, size) + bonus
                roll2 = random.randint(1, size) + bonus
                roll_result = max(roll1, roll2) if advantage else min(roll1, roll2)
                result1 += roll1
                result2 += roll2
            result += roll_result

        if size_throws == "1d20":
            result -= 2 * self.character_info[EXHAUSTION]
            result1 -= 2 * self.character_info[EXHAUSTION]
            result2 -= 2 * self.character_info[EXHAUSTION]

        if advantage:
            advdis = 1
        elif disadvantage:
            advdis = 2
        else:
            advdis = 0

        return result, advdis, result1, result2


    def toggle(self, hide, show, disable, enable):
        self.hide_widgets(hide)
        self.show_widgets(show)
        self.disable_widgets(disable)
        self.enable_widgets(enable)


    def update_abilities(self):
        method = self.abilityGeneration_comboBox.currentText()
        
        if method == STAT_GENERATION[0]: # Standard array
            availableStats = BASE_STATS.copy()
            availableStatsRandom = BASE_STATS.copy()
            for i, ability in enumerate(ABILITIES):
                stat = self.abilities_widgets[ability]["base_comboBox"].currentText()
                if stat and stat != RANDOM:
                    self.abilitiesBase[i] = int(stat)
                    self.abilitiesFixed[i] = 1
                    availableStats.remove(int(stat))
                    availableStatsRandom.remove(int(stat))
            for i, ability in enumerate(ABILITIES):
                stat = self.abilities_widgets[ability]["base_comboBox"].currentText()
                if stat == RANDOM:
                    self.abilitiesBase[i] = random.choice(availableStatsRandom)
                    availableStatsRandom.remove(self.abilitiesBase[i])
            for i, ability in enumerate(ABILITIES):
                stat = self.abilities_widgets[ability]["base_comboBox"].currentText()
                self.abilities_widgets[ability]["base_comboBox"].clear()
                if stat and stat != RANDOM:
                    self.abilities_widgets[ability]["base_comboBox"].addItems([RANDOM] + [str(x) for x in sorted(availableStats + [int(stat)])])
                else:
                    self.abilities_widgets[ability]["base_comboBox"].addItems([RANDOM] + [str(x) for x in sorted(availableStats)])
                self.abilities_widgets[ability]["base_comboBox"].setCurrentText(stat)

        elif method == STAT_GENERATION[1]: # Point buy
            for i, ability in enumerate(ABILITIES):
                self.abilitiesBase[i] = int(self.abilities_widgets[ability]["basePoint_label"].text())

            for i, ability in enumerate(ABILITIES):
                value = int(self.abilities_widgets[ability]["basePoint_label"].text())
                if value > 8:
                    self.abilities_widgets[ability]["minus_button"].setEnabled(True)
                else:
                    self.abilities_widgets[ability]["minus_button"].setEnabled(False)
                if (value < 13 and self.points > 0) or (value < 15 and self.points > 1):
                    self.abilities_widgets[ability]["plus_button"].setEnabled(True)
                else:
                    self.abilities_widgets[ability]["plus_button"].setEnabled(False)

            self.points_label.setText(f"{POINTS_TRANSLATE}: {self.points}")
            self.abilitiesFixed = [1, 1, 1, 1, 1, 1]

        elif method == STAT_GENERATION[2]: # Random
            self.abilitiesFixed = [1, 1, 1, 1, 1, 1]
            for i, ability in enumerate(ABILITIES):
                throws = []
                for _ in range(4):
                    result, _, _, _ = self.throw(0, "1d6")
                    throws.append(result)
                throws = sorted(throws) # 4d6 drop lowest
                # Sum the highest three throws
                self.abilitiesBase[i] = sum(throws[1:])
                self.abilities_widgets[ability]["base_label"].setText(str(self.abilitiesBase[i]))

        elif method == STAT_GENERATION[3]: # Custom (by default suggest the "optimal" distribution for the class)
            self.abilitiesBase = CLASSES[self.character_info["class"]].get("base abilities", [10, 10, 10, 10, 10, 10]).copy()
            self.abilitiesFixed = CLASSES[self.character_info["class"]].get("fixed abilities", [0, 0, 0, 0, 0, 0]).copy()

            for ability in ABILITIES:
                if self.abilities_widgets[ability]["base_lineEdit"].text():
                    self.abilitiesBase[ABILITIES.index(ability)] = int(self.abilities_widgets[ability]["base_lineEdit"].text())
                    self.abilitiesFixed[ABILITIES.index(ability)] = 1

        self.shuffle_abilities()


    def update_ability_bonus(self, ability = None, N = None):
        if ability and N:
            self.character_info[f"{ability}{N}"] = self.abilities_widgets[ability][f"checkBox{N}"].isChecked()

        ones = 0
        twos = 0
        for ability in ABILITIES:
            if ability not in self.abilitiesBonus:
                if self.abilities_widgets[ability]["checkBox1"].isEnabled():
                    self.abilities_widgets[ability]["checkBox1"].setEnabled(False)
                    self.character_info[ability + "1"] = False
                if self.abilities_widgets[ability]["checkBox2"].isEnabled():
                    self.abilities_widgets[ability]["checkBox2"].setEnabled(False)
                    self.character_info[ability + "2"] = False
            else:
                if self.abilities_widgets[ability]["checkBox1"].isChecked():
                    ones += 1
                elif self.abilities_widgets[ability]["checkBox2"].isChecked():
                    twos += 1

        for ability in ABILITIES:
            if ability in self.abilitiesBonus:
                if self.abilities_widgets[ability]["checkBox1"].isChecked() or (not self.abilities_widgets[ability]["checkBox2"].isChecked() and (ones + 2 * twos) < 3):
                    self.abilities_widgets[ability]["checkBox1"].setEnabled(True)
                else:
                    self.abilities_widgets[ability]["checkBox1"].setEnabled(False)
                
                if self.abilities_widgets[ability]["checkBox2"].isChecked() or (not self.abilities_widgets[ability]["checkBox1"].isChecked() and (ones + 2 * twos) < 2):
                    self.abilities_widgets[ability]["checkBox2"].setEnabled(True)
                else:
                    self.abilities_widgets[ability]["checkBox2"].setEnabled(False)

        self.update_ability_bonuses()
    
    def update_ability_bonuses(self):
        self.abilities = self.abilitiesBase.copy()
        for i in range(6):
            for j in range(5):
                self.abilities[i] += self.abilitiesFeats[j][i]

        for i, ability in enumerate(ABILITIES):
            if ability in self.abilitiesBonus:
                # Add values to the abilities
                if self.abilities_widgets[ability]["checkBox1"].isChecked():
                    self.abilities[i] += 1
                if self.abilities_widgets[ability]["checkBox2"].isChecked():
                    self.abilities[i] += 2

            self.abilities_widgets[ability]["final_label"].setText(str(self.abilities[i]))

            # Calculate the abilities bonuses
            self.ability_bonuses[i] = int(np.floor((self.abilities[i] - 10) / 2))
            self.abilities_widgets[ability]["label"].setText(f"{ability} ({self.ability_bonuses[i]})")
        
        if self.initiative_checkBox.isChecked():
            self.initiative_label.setText(f"{INITIATIVE_BONUS_TRANSLATE}: {self.ability_bonuses[1] + self.proficiency}")
        else:
            self.initiative_label.setText(f"{INITIATIVE_BONUS_TRANSLATE}: {self.ability_bonuses[1]}")

        self.update_armor_class()

        self.update_health_shown()
        self.update_skills_shown()
        for ability in ABILITIES:
            self.update_ability_save(ability)


    def update_ability_generation(self, method):
        for ability in ABILITIES:
            self.abilities_widgets[ability]["base_lineEdit"].hide()
            self.abilities_widgets[ability]["base_label"].hide()
            self.abilities_widgets[ability]["base_comboBox"].hide()
            self.abilities_widgets[ability]["basePoint_label"].hide()
            self.abilities_widgets[ability]["minus_button"].hide()
            self.abilities_widgets[ability]["plus_button"].hide()
        self.points_label.hide()

        if method == STAT_GENERATION[0]: # Standard array
            for ability in ABILITIES:
                self.abilities_widgets[ability]["base_comboBox"].show()

        elif method == STAT_GENERATION[1]: # Point buy
            for ability in ABILITIES:
                self.abilities_widgets[ability]["basePoint_label"].show()
                self.abilities_widgets[ability]["minus_button"].show()
                self.abilities_widgets[ability]["plus_button"].show()

            self.points_label.show()

        elif method == STAT_GENERATION[2]: # Random
            for ability in ABILITIES:
                self.abilities_widgets[ability]["base_label"].show()

        elif method == STAT_GENERATION[3]: # Custom (by default suggest the "optimal" distribution for the class)
            for ability in ABILITIES:
                self.abilities_widgets[ability]["base_lineEdit"].show()

        self.update_abilities()

    
    def update_ability_save(self, ability):
        i = ABILITIES.index(ability)
        if self.abilities_widgets[ability]["checkBox"].isChecked():
            self.abilities_widgets[ability]["saves_label"].setText(f"{ability}: {self.ability_bonuses[i] + self.proficiency}")
        else:
            self.abilities_widgets[ability]["saves_label"].setText(f"{ability}: {self.ability_bonuses[i]}")

        self.change()


    def update_advantage(self, advantage):
        self.advantage = advantage

        self.change()


    def update_age(self, age):
        self.character_info["age"] = age
        self.update_health_shown()
        self.update_heightWeight_shown()


    def update_age_shown(self):
        if self.character_info["ageMax"]:
            ageMax = self.character_info["ageMax"]
        else:
            ageMax = self.ageMax_lineEdit.placeholderText()

        if self.character_info["ageMin"]:
            ageMin = self.character_info["ageMin"]
        else:
            ageMin = self.ageMin_lineEdit.placeholderText()

        if ageMin and ageMax:
            if not ageMin == ageMax:
                if int(ageMax) < self.ageMax:
                    # Generate weights - linearly decreasing
                    weights = [self.ageMax + 10 * self.ageMax / (age - self.ageMax) for age in range(int(ageMin), int(ageMax) + 1)] # Might not be particularly efficient, as it generates a pretty long list
                else:
                    weights = [1] * (int(ageMax) - int(ageMin) + 1)

                # Select an age based on weights
                age = str(random.choices(range(int(ageMin), int(ageMax) + 1), weights, k = 1)[0])
            else:
                age = ageMin
            self.age_lineEdit.setPlaceholderText(str(age))
        else:
            self.age_lineEdit.setPlaceholderText("")

        self.update_health_shown()
        self.update_heightWeight_shown()


    def update_ageMax(self, ageMax):
        self.character_info["ageMax"] = ageMax
        if self.character_info["ageMin"]:
            ageMin = self.character_info["ageMin"]
        else:
            ageMin = self.ageMin_lineEdit.placeholderText()

        if ageMax and ageMin:
            if int(ageMax) < int(ageMin):
                if self.character_info["ageMin"]:
                    self.character_info["ageMin"] = ageMax
                    self.ageMin_lineEdit.setText(str(ageMax))
                else:
                    self.ageMin_lineEdit.setPlaceholderText(str(ageMax))

        self.update_age_shown()


    def update_ageMin(self, ageMin):
        self.character_info["ageMin"] = ageMin
        self.update_age_shown()


    def update_ageMinMax_shown(self):
        ageMax = self.species_ageMax + self.lineage_ageMax
        if ageMax:
            self.ageMax_lineEdit.setPlaceholderText(str(ageMax))
        else:
            self.ageMax_lineEdit.setPlaceholderText("")

        self.ageMax = ageMax * 5 / 4

        if self.character_info["ageMax"] and int(self.character_info["ageMax"]) < self.developmentAge:
            ageMin = self.character_info["ageMax"]
        else:
            ageMin = self.developmentAge

        if ageMin:
            self.ageMin_lineEdit.setPlaceholderText(str(ageMin))
        else:
            self.ageMin_lineEdit.setPlaceholderText("")

        self.update_age_shown()


    def update_alignment(self, alignment):
        self.character_info["alignment"] = alignment

        self.change()


    def update_armor(self, armor, state, shield=False):
        if not shield:
            if state:
                self.character_info["armor"] = armor
            else:
                self.character_info["armor"] = None
            for widgets in self.equipment_widgets:
                if widgets:
                    element = widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "")
                    if element in ARMORS and element != armor: # Disabling/enabling all other armors when an armor is equipped/unequipped
                        if state:
                            widgets["checkBox2"].setEnabled(False)
                        else:
                            widgets["checkBox2"].setEnabled(True)
                            
        else:
            if state:
                self.character_info["offHand"] = armor
            else:
                self.character_info["offHand"] = None
            for widgets in self.equipment_widgets:
                if widgets:
                    element = widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "")
                    if element != armor: # Disabling/enabling all other off hand items when a shield is equipped/unequipped
                        if (self.character_info["mainHand"] and element in WEAPONS and LIGHT in WEAPONS[element]["properties"] and (not widgets["checkBox2"].isChecked() or self.character_info["equipment"][element] > 1)) or element in WEAPON_TYPES[SHIELDS_TRANSLATE]:    
                            if state:
                                widgets["checkBox1"].setEnabled(False)
                            else:
                                widgets["checkBox1"].setEnabled(True)
                        elif state and not (element in WEAPONS and LIGHT in WEAPONS[element]["properties"]):
                            widgets["checkBox2"].setEnabled(False)
                        elif not self.character_info["mainHand"]:
                            widgets["checkBox2"].setEnabled(True)

        self.update_armor_class()


    def update_armor_class(self):
        offHand = self.character_info["offHand"]
        shield = 0
        if offHand in WEAPON_TYPES[SHIELDS_TRANSLATE]:
            for weapon_proficiency in self.proficiencies:
                if isinstance(weapon_proficiency, str) and weapon_proficiency in WEAPON_TYPES and offHand in WEAPON_TYPES[weapon_proficiency]:
                    shield = ARMORS[offHand]["armor class"]
                    break

        modifier = 1 # Bonus will be given by dexterity

        armor = self.character_info["armor"]
        if armor:
            armor = ARMORS[armor]
            base = armor["armor class"]
            maximum = armor["dexterity"]
            if not (maximum + 1):
                maximum = 10

            strength = armor["strength"]
            if strength and self.ability_bonuses[0] < strength:
                self.armor_speed = -10
            else:
                self.armor_speed = 0
                
            if armor["stealth"]:
                self.armor_disadvantages = [STEALTH]
            else:
                self.armor_disadvantages = []

            proficiency = False
            for weapon_proficiency in self.proficiencies:
                if isinstance(weapon_proficiency, str) and weapon_proficiency in WEAPON_TYPES and armor in WEAPON_TYPES[weapon_proficiency]:
                    proficiency = True
                    break

            if not proficiency:
                self.armor_disadvantages.extend([STRENGTH, DEXTERITY])

            if HEAVY in armor["tags"] and self.raging:
                button: QPushButton = self.features_widgets[CLASS_FEATURES_INDEX * MAX_NUM + 1][4]
                button.click()
        else:
            base = 10
            maximum = 10
            self.armor_speed = 0
            self.armor_disadvantages = []

        self.update_speed()
        self.update_resistances()

        if not self.attributes["Unarmored Defense"] or self.character_info["armor"]:
            modifier = min(self.ability_bonuses[modifier], maximum)
        else:
            modifier = self.ability_bonuses[1] + self.ability_bonuses[2]

        self.armorClass_label.setText(f"{ARMOR_CLASS_TRANSLATE}: {base + modifier + shield}")


    def update_available_languages(self, languages):
        if languages == STANDARD_TRANSLATE:
            self.character_info["available languages"] = [RANDOM] + STANDARD_LANGUAGES
        else:
            self.character_info["available languages"] = [RANDOM, RANDOM_STANDARD, RANDOM_RARE] + STANDARD_LANGUAGES + RARE_LANGUAGES
        self.update_languages()
        
    
    def update_background(self, background):
        self.character_info["background"] = background
        self.abilitiesBonus = BACKGROUNDS[background].get("abilities bonus", [0, 0, 0, 0, 0, 0])
        self.update_ability_bonus()

        self.background_skill_proficiencies = BACKGROUNDS[background].get("skill proficiencies", [])
        self.update_skill_proficiencies()

        self.background_tool_proficiencies = BACKGROUNDS[background].get("tool proficiencies", [])
        self.update_proficiencies()

        self.background_equipment = BACKGROUNDS[background].get("equipment", [])
        self.update_equipment()

        background_feat = BACKGROUNDS[background].get("feat", "")
        if background_feat:
            self.feats_widgets[0]["label"].show()
            self.feats_widgets[0]["comboBox"].show()
            self.feats_widgets[0]["comboBox"].setCurrentText(background_feat)
        else:
            self.feats_widgets[0]["label"].hide()
            self.feats_widgets[0]["comboBox"].hide()
            self.feats_widgets[0]["comboBox"].setCurrentText(RANDOM)
        self.update_feat(0)

        self.update_disable_enable() # Needed to update which comboBoxes need to be enabled/disabled


    def update_blinded(self, checked):
        self.character_info[BLINDED] = checked


    def update_charmed(self, checked):
        self.character_info[CHARMED] = checked


    def update_class(self, classe): # Class is a reserved word
        for attribute in self.attributes.keys():
            if "attributes" in CLASSES[classe] and attribute in CLASSES[classe]["attributes"]:
                self.attributes[attribute] = True
            else:
                self.attributes[attribute] = False
        self.character_info["class"] = classe

        if len(CLASSES[classe]["subclasses"]) != 1:
            self.subclass_comboBox.show()
            self.subclass_label.show()
        else:
            self.subclass_comboBox.hide()
            self.subclass_label.hide()

        subclass = self.subclass_comboBox.currentText()
        self.subclass_comboBox.clear()
        self.subclass_comboBox.addItems(CLASSES[classe]["subclasses"])
        self.subclass_comboBox.setCurrentText(subclass)

        if classe != RANDOM and classe != NONE_F:
            self.level_comboBox.show()
            self.level_label.show()
        else:
            self.level_comboBox.hide()
            self.level_label.hide()
        
        level = self.level_comboBox.currentText()
        self.level_comboBox.clear()
        self.level_comboBox.addItems(LEVELS.get(self.character_info["class"], LEVELS_CLASS)) # If a class is selected, it defaluts to level from 1 to 20
        self.level_comboBox.setCurrentText(level)

        self.hitDie = CLASSES[classe].get("health dice", 0)
        self.hitDieFixed = self.hitDie/2 + 1
        self.update_level(self.level_comboBox.currentText())

        self.class_weapon_proficiencies = CLASSES[classe].get("weapon proficiencies", [])
        self.class_armor_proficiencies = CLASSES[classe].get("armor proficiencies", [])
        self.class_tool_proficiencies = CLASSES[classe].get("tool proficiencies", [])

        self.class_skill_proficiencies = CLASSES[classe].get("skill proficiencies", [])

        self.class_condition_advantages = CLASSES[classe].get("condition advantages", [])
        self.class_condition_immunities = CLASSES[classe].get("condition immunities", [])
        self.class_save_advantages = CLASSES[classe].get("save advantages", [])
        self.class_resistances = CLASSES[classe].get("resistances", [])

        self.class_misc = CLASSES[classe].get("misc", [])

        self.class_magic = CLASSES[classe].get("magic", [])

        save_proficiencies = CLASSES[classe].get("save proficiencies", [])
        for ability in ABILITIES:
            if ability in save_proficiencies:
                self.abilities_widgets[ability]["checkBox"].setChecked(True)
            else:
                self.abilities_widgets[ability]["checkBox"].setChecked(False)

        for ability in ABILITIES:
            self.update_ability_save(ability)

        self.class_equipment = CLASSES[classe].get("equipment", [])

        self.update_subclass(self.subclass_comboBox.currentText())

        self.update_equipment()

        if self.abilityGeneration_comboBox.currentText() == STAT_GENERATION[3]:
            self.update_abilities()

        self.update_disable_enable() # Needed to update which comboBoxes need to be enabled/disabled

        self.features[CLASS_FEATURES_INDEX] = CLASSES[classe].get("features", [""])
        self.update_features(CLASS_FEATURES_INDEX)

    
    def update_copper(self, copper):
        self.character_info["copper"] = copper
        self.change()


    def update_darkvision(self):
        darkvision = self.species_darkvision + self.lineage_darkvision
        if darkvision or darkvision == 0:
            self.darkvision_label.setText(DARKVISION_TRANSLATE + ": %.1f " %darkvision + LENGTH_UNIT)
        else:
            self.darkvision_label.setText(DARKVISION_TRANSLATE + ":")

        self.change()


    def update_deafened(self, checked):
        self.character_info[DEAFENED] = checked
                
                
    def update_disable_enable(self):
        self.disableEnable = (
            self.languages_widgets + self.proficiencies_widgets +
            [i["checkBox1"] for i in self.abilities_widgets.values()] +
            [i["checkBox2"] for i in self.abilities_widgets.values()] +
            [i["checkBox"] for i in self.skills_widgets.values()] +
            [i["final_label"] for i in self.abilities_widgets.values()] +
            [i["comboBox"] for i in self.feats_widgets.values() if i] +
            [i["comboBox1"] for i in self.feats_widgets.values() if i] +
            [i["comboBox2"] for i in self.feats_widgets.values() if i] +
            [self.health_lineEdit] +
            [self.health_checkBox] +
            [self.species_comboBox] +
            [self.lineage_comboBox] +
            [self.background_comboBox] +
            [self.class_comboBox] +
            [self.subclass_comboBox] +
            [self.level_comboBox] +
            [self.alignment_comboBox] +
            [self.name_lineEdit] +
            [self.gender_comboBox] +
            [self.age_lineEdit] +
            [self.height_lineEdit] +
            [self.weight_lineEdit] +
            [self.equipmentFixed_checkBox]
        )


    def update_electrum(self, electrum):
        self.character_info["electrum"] = electrum
        self.change()


    def update_equipment(self):
        if not self.character_info["equipmentFixed"]: # If the base equipment is getting changed, it will be calculated from the start
            equipment = self.class_equipment + self.background_equipment
            if self.extra_equipment and self.extra_equipment != RANDOM:
                equipment += [self.extra_equipment]

            for widgets in self.equipment_widgets:
                if widgets:
                    for widget in widgets.values():
                        if isinstance(widget, QWidget):
                            widget.hide()
                            widget.deleteLater()
            self.equipment_widgets = []
            self.character_info["equipment"] = {}
            self.add_equipment(UNARMED_STRIKE, False, 0, 0, 0)
            self.add_equipment(IMPROVISED_WEAPON, False, 0, 0, 0)

            # Removing coins added by default equipment
            if self.addedCopper:
                self.copper_lineEdit.setText(str(int(self.copper_lineEdit.text()) - self.addedCopper))
                if int(self.copper_lineEdit.text()) < 0:
                    self.copper_lineEdit.setText("0")
                self.addedCopper = 0
                self.update_copper(self.copper_lineEdit.text())
            if self.addedSilver:
                self.silver_lineEdit.setText(str(int(self.silver_lineEdit.text()) - self.addedSilver))
                if int(self.silver_lineEdit.text()) < 0:
                    self.silver_lineEdit.setText("0")
                self.addedSilver = 0
                self.update_silver(self.silver_lineEdit.text())
            if self.addedElectrum:
                self.electrum_lineEdit.setText(str(int(self.electrum_lineEdit.text()) - self.addedElectrum))
                if int(self.electrum_lineEdit.text()) < 0:
                    self.electrum_lineEdit.setText("0")
                self.addedElectrum = 0
                self.update_electrum(self.electrum_lineEdit.text())
            if self.addedGold:
                self.gold_lineEdit.setText(str(int(self.gold_lineEdit.text()) - self.addedGold))
                if int(self.gold_lineEdit.text()) < 0:
                    self.gold_lineEdit.setText("0")
                self.addedGold = 0
                self.update_gold(self.gold_lineEdit.text())
            if self.addedPlatinum:
                self.platinum_lineEdit.setText(str(int(self.platinum_lineEdit.text()) - self.addedPlatinum))
                if int(self.platinum_lineEdit.text()) < 0:
                    self.platinum_lineEdit.setText("0")
                self.addedPlatinum = 0
                self.update_platinum(self.platinum_lineEdit.text())

            for element in equipment:
                if isinstance(element, str) and len (element.split("MULTIPLE ")) > 1:
                    self.add_equipment(element.split("MULTIPLE ")[-1])
                    self.update_equipment_quantity(element.split("MULTIPLE ")[-1], element.split("MULTIPLE ")[0])
                elif isinstance(element, str):
                    self.add_equipment(element)
                else:
                    if [element] == self.class_equipment:
                        xx = 0
                    elif [element] == self.background_equipment:
                        xx = 1
                    else:
                        xx = 0
                    self.add_equipment(element, False, xx)


    def update_equipment_extra(self, equipment):
        self.extra_equipment = equipment
        self.update_equipment()


    def update_equipment_fixed(self, equipmentFixed):
        self.character_info["equipmentFixed"] = equipmentFixed

        for i, widgets in enumerate(self.equipment_widgets):
            if widgets and "comboBox" in widgets and widgets["comboBox"]:
                if widgets["comboBox"].currentText() == RANDOM:
                    if len(self.character_info["equipmentBase"][widgets["xx"]]) > widgets["yy"] and self.character_info["equipmentBase"][widgets["xx"]][widgets["yy"]] and self.character_info["equipmentBase"][widgets["xx"]][widgets["yy"]] != RANDOM:
                        widgets["comboBox"].setCurrentText(self.character_info["equipmentBase"][widgets["xx"]][widgets["yy"]])
                    else:
                        possibilities = [widgets["comboBox"].itemText(j) for j in range(1, widgets["comboBox"].count())]
                        random.shuffle(possibilities)
                        widgets["comboBox"].setCurrentText(possibilities[0])
                self.add_optional_equipment(i, widgets["comboBox"].currentText())                    

        self.update_equipment()


    def update_equipment_quantity(self, element, quantity):
        if element in self.character_info["equipment"] and element != UNARMED_STRIKE and element != IMPROVISED_WEAPON:
            if int(quantity):
                self.character_info["equipment"][element] = int(quantity)
                for widgets in self.equipment_widgets:
                    if widgets and widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "") == element:
                        widgets["lineEdit"].setText(str(quantity))
                        if int(quantity) > 1 and element in WEAPONS and LIGHT in WEAPONS[element]["properties"] and self.character_info["mainHand"]:
                            widgets["checkBox1"].setEnabled(True)
                        else:
                            widgets["checkBox1"].setEnabled(False)
            else:
                self.character_info["equipment"].pop(element)
                i = 0
                while not self.equipment_widgets[i] or self.equipment_widgets[i]["label"].text().replace("<u>", "").replace("</u>", "") != element:
                    i += 1
                for widget in self.equipment_widgets[i].values():
                    widget.hide()
                    widget.deleteLater()
                self.equipment_widgets[i] = None

            for i in range(LEN_FEATURES):
                self.update_features(i, False)

            self.change()


    def update_exhaustion(self, value):
        self.character_info[EXHAUSTION] = value
        self.update_speed()
        if value == 6:
            self.update_health_current("0")


    def update_feat(self, i):
        if self.choosingFeat: # This happens when levelling up and reaching a feat level
            for newFeat in range(2, LEN_FEATS):
                if self.feats_widgets[newFeat]["comboBox"].isHidden():
                    newFeat -= 1
                    break

            feat = self.feats_widgets[newFeat]["comboBox"].currentText()

            if "bonus ability 1" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 1"], list):
                self.feats_widgets[newFeat]["comboBox1"].setEditable(False)
                self.feats_widgets[newFeat]["comboBox2"].setEditable(False)
                self.feats_widgets[newFeat]["comboBox1"].enableWheel()
                self.feats_widgets[newFeat]["comboBox2"].enableWheel()
            elif "bonus ability 2" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 2"], list):
                self.feats_widgets[newFeat]["comboBox1"].setEditable(False)
                self.feats_widgets[newFeat]["comboBox1"].enableWheel()

        self.character_info["feats"][i] = self.feats_widgets[i]["comboBox"].currentText()
        self.character_info["feats1"][i] = self.feats_widgets[i]["comboBox1"].currentText()
        self.character_info["feats2"][i] = self.feats_widgets[i]["comboBox2"].currentText()
        self.abilitiesFeats[i] = [0, 0, 0, 0, 0, 0]

        feat = self.feats_widgets[i]["comboBox"].currentText()
        if not feat:
            feat = RANDOM
        if not self.feats_widgets[i]["comboBox"].isHidden() and "bonus ability 1" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 1"], list):
            self.feats_widgets[i]["comboBox1"].show()
            self.feats_widgets[i]["comboBox2"].show()
        elif not self.feats_widgets[i]["comboBox"].isHidden() and "bonus ability 2" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 2"], list):
            self.feats_widgets[i]["comboBox1"].show()
            self.feats_widgets[i]["comboBox2"].hide()
            self.feats_widgets[i]["comboBox2"].setCurrentIndex(0)
        else:
            self.feats_widgets[i]["comboBox1"].hide()
            self.feats_widgets[i]["comboBox1"].setCurrentIndex(0)
            self.feats_widgets[i]["comboBox2"].hide()
            self.feats_widgets[i]["comboBox2"].setCurrentIndex(0)

        self.features[i] = FEATS[feat].get("features", [""])
        self.update_features(i)

        if "bonus ability 1" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 1"], str) and not self.feats_widgets[i]["comboBox"].isHidden():
            self.abilitiesFeats[i][ABILITIES.index(FEATS[feat]["bonus ability 1"])] += 1
        elif "bonus ability 1" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 1"], list) and not self.feats_widgets[i]["comboBox"].isHidden():
            if self.feats_widgets[i]["comboBox1"].currentText() != RANDOM:
                self.abilitiesFeats[i][ABILITIES.index(self.feats_widgets[i]["comboBox1"].currentText())] += 1
            if self.feats_widgets[i]["comboBox2"].currentText() != RANDOM:
                self.abilitiesFeats[i][ABILITIES.index(self.feats_widgets[i]["comboBox2"].currentText())] += 1
        elif "bonus ability 2" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 2"], str) and not self.feats_widgets[i]["comboBox"].isHidden():
            self.abilitiesFeats[i][ABILITIES.index(FEATS[feat]["bonus ability 2"])] += 2
        elif "bonus ability 2" in FEATS[feat] and isinstance(FEATS[feat]["bonus ability 2"], list) and not self.feats_widgets[i]["comboBox"].isHidden():
            if self.feats_widgets[i]["comboBox1"].currentText() != RANDOM:
                self.abilitiesFeats[i][ABILITIES.index(self.feats_widgets[i]["comboBox1"].currentText())] += 2

        if "initiative proficiency" in FEATS[feat]:
            self.initiative_proficiency[i] = True
        else:
            self.initiative_proficiency[i] = False

        if any(self.initiative_proficiency):
            self.initiative_checkBox.setChecked(True)
            self.initiative_checkBox.show()
        else:
            self.initiative_checkBox.setChecked(False)
            self.initiative_checkBox.hide()

        self.feats_tool_proficiencies[i] = FEATS[feat].get("tool proficiencies", [])
        self.update_proficiencies()

        if not self.feats_widgets[i]["comboBox"].isHidden():
            self.feats_misc[i] = FEATS[feat].get("misc", [])
        else:
            self.feats_misc[i] = []
        self.update_misc()

        self.update_ability_bonuses()

        feats = []
        for widgets in self.feats_widgets.values():
            feat = widgets["comboBox"].currentText()
            items = [widgets["comboBox"].itemText(j) for j in range(widgets["comboBox"].count()) if widgets["comboBox"].itemText(j) not in feats]
            widgets["comboBox"].clear()
            widgets["comboBox"].addItems(items)
            widgets["comboBox"].setCurrentText(feat)

            if feat in feats and feat in self.character_info["feats"]:
                index = len(self.character_info["feats"]) - 1 - self.character_info["feats"][::-1].index(feat)
                self.character_info["feats"][index] = RANDOM
            if feat and not FEATS[feat]["repeatable"]:
                feats.append(feat)

    
    def update_features(self, index: int, changing=True):
        grid = self.features_grid

        if changing:
            for i in range(MAX_NUM):
                for widgets in self.features_widgets[index * MAX_NUM + i][1:]:
                    if isinstance(widgets, QWidget):
                        widgets.hide()
                        widgets.deleteLater()
                self.features_widgets[index * MAX_NUM + i] = []

        offset = 0
        shift = 0

        level = self.character_info["level"]

        for feature in self.features[index]:

            if isinstance(feature, list):

                if level != RANDOM and int(level) >= feature[0]:
                    feature = feature[1]
                    if len(self.features_widgets) <= (index * MAX_NUM + shift) or len(self.features_widgets[index * MAX_NUM + shift]) == 1:
                        changing = True
                else:
                    label = HoverableLabel(f"{feature[1]} ({LEVEL_TRANSLATE} {feature[0]})")
                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    self.features_widgets[index * MAX_NUM + shift] = [label]

        # Here starts a long if chain. Not sure how to remove it. There are some possibilities, but I'll see depending on how different features are. For now they are quite different, so I have no hope of making, say, just 10 different cases. I could do like a "function dictionary", but I'll see. Anyhow, it needs to be changed, probably
            if isinstance(feature, list):
                pass

            elif feature == RAGE:
                if level == RANDOM or int(level) < 3:
                    number = 2
                elif int(level) < 6:
                    number = 3
                elif int(level) < 12:
                    number = 4
                elif int(level) < 17:
                    number = 5
                else:
                    number = 6
                    
                if changing:
                    label = HoverableLabel(f"{RAGE} ({number}/{LONG_REST_TRANSLATE}, +1/{SHORT_REST_TRANSLATE})")
                    
                    if self.character_info["points"][index * MAX_NUM + shift]:
                        button = QPushButton(self.character_info["points"][index * MAX_NUM + shift], default=False, autoDefault=False)
                    else:
                        button = QPushButton(str(number), default=False, autoDefault=False)
                    button.setFixedWidth(100)
                    
                    button2 = QPushButton(FINISH_TRANSLATE, default=False, autoDefault=False)
                    button2.setFixedWidth(100)

                    space2 = QLabel("")
                    space2.setFixedWidth(100)
                    
                    def effect(index, shift):
                        if int(button.text()) and not (self.character_info["armor"] and HEAVY in ARMORS[self.character_info["armor"]]["tags"]):
                            self.raging = True
                            button.setEnabled(False)
                            button.setFocus()
                            button2.show()
                            self.features_resistances[index * MAX_NUM + shift] = [BLUDGEONING, PIERCING, SLASHING]
                            self.features_save_advantages[index * MAX_NUM + shift] = [STRENGTH]
                            self.features_throw_advantages[index * MAX_NUM + shift] = [STRENGTH]
                            self.character_info["points"][index * MAX_NUM + shift] = str(int(button.text()) - 1)
                            button.setText(str(int(button.text()) - 1))
                            self.update_resistances()

                    def effect2(index, shift):
                        self.raging = False
                        button.setEnabled(True)
                        button2.hide()
                        button2.setFocus()
                        self.features_resistances[index * MAX_NUM + shift] = []
                        self.features_save_advantages[index * MAX_NUM + shift] = []
                        self.features_throw_advantages[index * MAX_NUM + shift] = []
                        self.update_resistances()

                    button.clicked.connect(lambda _, i=index, s=shift: effect(i, s))
                    button2.clicked.connect(lambda _, i=index, s=shift: effect2(i, s))

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    grid.addWidget(button, offset + MAX_HEIGHT * index + 1, 0)
                    grid.addWidget(space2, offset + MAX_HEIGHT * index + 1, 1)
                    grid.addWidget(button2, offset + MAX_HEIGHT * index + 1, 1)

                    button2.hide()

                    self.features_widgets[index * MAX_NUM + shift] = ["Short Rest +1", label, button, number, button2, space2]
                else:
                    label = self.features_widgets[index * MAX_NUM + shift][1]
                    button2: QPushButton = self.features_widgets[index * MAX_NUM + shift][4]
                    self.features_widgets[index * MAX_NUM + shift][3] = number

                    label.setText(f"{RAGE} ({number}/{LONG_REST_TRANSLATE}, +1/{SHORT_REST_TRANSLATE})")

                    if self.character_info[INCAPACITATED]:
                        button2.click()

            elif feature == WEAPON_MASTERY_TRANSLATE:
                if changing:
                    label = HoverableLabel(f"{WEAPON_MASTERY_TRANSLATE} ({CHANGE_ON_TRANSLATE} {LONG_REST_TRANSLATE})")
                    
                    comboBox1 = HoverableComboBox()
                    comboBox1.addItems([RANDOM] + WEAPON_MASTERIES)
                    comboBox1.setFixedWidth(100)
                    comboBox2 = HoverableComboBox()
                    comboBox2.addItems([RANDOM] + WEAPON_MASTERIES)
                    comboBox2.setFixedWidth(100)
                    comboBox3 = HoverableComboBox()
                    comboBox3.addItems([RANDOM] + WEAPON_MASTERIES)
                    comboBox3.setFixedWidth(100)
                    comboBox4 = HoverableComboBox()
                    comboBox4.addItems([RANDOM] + WEAPON_MASTERIES)
                    comboBox4.setFixedWidth(100)
                    comboBox5 = HoverableComboBox()
                    comboBox5.addItems([RANDOM] + WEAPON_MASTERIES)
                    comboBox5.setFixedWidth(100)
                    comboBox6 = HoverableComboBox()
                    comboBox6.addItems([RANDOM] + WEAPON_MASTERIES)
                    comboBox6.setFixedWidth(100)

                    comboBoxes = [comboBox1, comboBox2, comboBox3, comboBox4, comboBox5, comboBox6]
                    
                    classe = self.character_info["class"]
                    level = self.character_info["level"]
                    if not level.isdigit():
                        level = 0
                    masteries = CLASSES[classe]["masteries"][int(level)]
                    if masteries < len(comboBoxes):
                        for comboBox in comboBoxes[masteries:]:
                            comboBox.hide()

                    button = QPushButton("", default=False, autoDefault=False)
                    button.hide()

                    def effect():
                        for comboBox in comboBoxes:
                            if comboBox.currentText() == RANDOM:
                                possibilities = [comboBox.itemText(j) for j in range(1, comboBox.count())]
                                random.shuffle(possibilities)
                                comboBox.setCurrentText(possibilities[0])
                            comboBox.setEditable(True)
                            comboBox.lineEdit().setReadOnly(True)
                            comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                            comboBox.disableWheel()
                            font = comboBox.lineEdit().font()
                            font.setPointSize(int(12 * scale_factor))
                            comboBox.lineEdit().setFont(font)
                    
                    button.clicked.connect(effect)

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0, 1, 3)
                    grid.addWidget(button, offset + MAX_HEIGHT * index + 1, 0) # Placed below the comboBox as will be triggered by the stopRest_button
                    grid.addWidget(comboBox1, offset + MAX_HEIGHT * index + 1, 0)
                    grid.addWidget(comboBox2, offset + MAX_HEIGHT * index + 1, 1)
                    grid.addWidget(comboBox3, offset + MAX_HEIGHT * index + 1, 2)
                    grid.addWidget(comboBox4, offset + MAX_HEIGHT * index + 1, 3)
                    grid.addWidget(comboBox5, offset + MAX_HEIGHT * index + 1, 4)
                    grid.addWidget(comboBox6, offset + MAX_HEIGHT * index + 1, 5)

                    self.features_widgets[index * MAX_NUM + shift] = ["Long Rest after", label] + comboBoxes + [button]
                else:
                    comboBoxes = [self.features_widgets[index * MAX_NUM + shift][i] for i in range(2, 8)]
                    classe = self.character_info["class"]
                    level = self.character_info["level"]
                    if not level.isdigit():
                        level = 0
                    masteries = CLASSES[classe]["masteries"][int(level)]
                    if masteries < len(comboBoxes):
                        for comboBox in comboBoxes[:masteries]:
                            comboBox.show()
                        for comboBox in comboBoxes[masteries:]:
                            comboBox.hide()

            elif feature == QUICK_CRAFTING:
                if changing:
                    label = HoverableLabel(f"{QUICK_CRAFTING} ({LONG_REST_TRANSLATE})")
                    comboBox = HoverableComboBox()
                    comboBox.addItems([NONE_M, RANDOM])
                    comboBox.setFixedWidth(100)

                    def effect():
                        if comboBox.currentText() != NONE_M:
                            if comboBox.currentText() == RANDOM:
                                possibilities = [comboBox.itemText(j) for j in range(2, comboBox.count())]
                                random.shuffle(possibilities)
                                comboBox.setCurrentText(possibilities[0])
                            self.longRest_remove.append(comboBox.currentText() + "*")
                            self.add_equipment(comboBox.currentText() + "*")
                            comboBox.setEditable(True)
                            comboBox.lineEdit().setReadOnly(True)
                            comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                            comboBox.disableWheel()
                            font = comboBox.lineEdit().font()
                            font.setPointSize(int(12 * scale_factor))
                            comboBox.lineEdit().setFont(font)
                            self.stopRest_button.hide()
                    
                    comboBox.activated.connect(effect)

                    for key in FAST_CRAFTING.keys():
                        for item in FAST_CRAFTING[key]:
                            if item in list(self.character_info["equipment"].keys()) and item in self.character_info["proficiencies"]:
                                comboBox.addItem(item)

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    grid.addWidget(comboBox, offset + MAX_HEIGHT * index + 1, 0)

                    self.features_widgets[index * MAX_NUM + shift] = ["Long Rest after", label, comboBox]
                
                    comboBox.setEditable(True)
                    comboBox.lineEdit().setReadOnly(True)
                    comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                    comboBox.disableWheel()
                    font = comboBox.lineEdit().font()
                    font.setPointSize(int(12 * scale_factor))
                    comboBox.lineEdit().setFont(font)
                else:
                    label = self.features_widgets[index * MAX_NUM + shift][1]
                    comboBox = self.features_widgets[index * MAX_NUM + shift][2]
                    comboBox.clear()
                    comboBox.addItems([NONE_M, RANDOM])
                    for key in FAST_CRAFTING.keys():
                        for item in FAST_CRAFTING[key]:
                            if key in list(self.character_info["equipment"].keys()) and key in self.character_info["proficiencies"]:
                                comboBox.addItem(item)

                if comboBox.count() > 2:
                    comboBox.show()
                    label.show()
                else:
                    comboBox.hide()
                    label.hide()

            elif feature == HEALING_HANDS:
                if changing:
                    label = HoverableLabel(f"{HEALING_HANDS} ({LONG_REST_TRANSLATE})")

                    button = QPushButton(THROW_TRANSLATE, default=False, autoDefault=False)
                    button.setFixedWidth(100)

                    def effect():
                        self.dice(0, f"{self.proficiency}d4")
                        button.setEnabled(False)
                        button.setFocus()

                    button.clicked.connect(effect)

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    grid.addWidget(button, offset + MAX_HEIGHT * index + 1, 0)

                    self.features_widgets[index * MAX_NUM + shift] = [LONG_REST_TRANSLATE, label, button]
                else:
                    button = self.features_widgets[index * MAX_NUM + shift][2]
                    button.setText(THROW_TRANSLATE)
                    button.setStyleSheet("""
                        QPushButton {
                            background-color: 2d2d2d;
                            border: 1px solid #d3d3d3; 
                            border-radius: 5px;                                      
                        }
                    """)

            elif feature == CELESTIAL_REVELATION:
                if changing:
                    label = HoverableLabel(f"{CELESTIAL_REVELATION} ({LONG_REST_TRANSLATE})")
                    comboBox = HoverableComboBox()
                    comboBox.addItems([NONE_M, HEAVENLY_WINGS, INNER_RADIANCE, NECROTIC_SHROUD])
                    comboBox.setFixedWidth(200)

                    button = QPushButton(CONFIRM_TRANSLATE, default=False, autoDefault=False)
                    button.setFixedWidth(100)

                    label2 = HoverableLabel("")
                    label3 = HoverableLabel("")
                    label.setFixedWidth(200)
                    label2.setFixedWidth(100)
                    label3.setFixedWidth(200)
                    button2 = QPushButton(FINISH_TRANSLATE, default=False, autoDefault=False)
                    button2.setFixedWidth(100)

                    def effect():
                        if comboBox.currentText() == HEAVENLY_WINGS:
                            self.flySpeed_label.setText(f"{FLY_SPEED_TRANSLATE}: {self.speed:.1f}{LENGTH_UNIT}")
                            self.flySpeed_label.show()
                            label2.setText(f"+{self.proficiency} {RADIANT}")
                            button.setEnabled(False)
                            button.setFocus()
                            button2.show()
                            comboBox.setEditable(True)
                            comboBox.lineEdit().setReadOnly(True)
                            comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                            comboBox.disableWheel()
                            font = comboBox.lineEdit().font()
                            font.setPointSize(int(12 * scale_factor))
                            comboBox.lineEdit().setFont(font)
                        elif comboBox.currentText() == INNER_RADIANCE:
                            label2.setText(f"+{self.proficiency} {RADIANT}")
                            button.setEnabled(False)
                            button.setFocus()
                            button2.show()
                            comboBox.setEditable(True)
                            comboBox.lineEdit().setReadOnly(True)
                            comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                            comboBox.disableWheel()
                            font = comboBox.lineEdit().font()
                            font.setPointSize(int(12 * scale_factor))
                            comboBox.lineEdit().setFont(font)
                        elif comboBox.currentText() == NECROTIC_SHROUD:
                            label2.setText(f"+{self.proficiency} {NECROTIC}")
                            label3.setText(f"{SAVE_THROW_TRANSLATE} {CHARISMA} {DC_TRANSLATE} {8 + self.proficiency + self.ability_bonuses[5]}")
                            button.setEnabled(False)
                            button.setFocus()
                            button2.show()
                            comboBox.setEditable(True)
                            comboBox.lineEdit().setReadOnly(True)
                            comboBox.lineEdit().setCursor(Qt.CursorShape.IBeamCursor)
                            comboBox.disableWheel()
                            font = comboBox.lineEdit().font()
                            font.setPointSize(int(12 * scale_factor))
                            comboBox.lineEdit().setFont(font)

                    def effect2():
                        button2.hide()
                        button2.setFocus()
                        label2.setText("")
                        label3.setText("")
                        self.flySpeed_label.hide()

                    button.clicked.connect(effect)
                    button2.clicked.connect(effect2)

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    grid.addWidget(label2, offset + MAX_HEIGHT * index, 1)
                    grid.addWidget(label3, offset + MAX_HEIGHT * index, 2)
                    grid.addWidget(comboBox, offset + MAX_HEIGHT * index + 1, 0)
                    grid.addWidget(button, offset + MAX_HEIGHT * index + 1, 1)
                    grid.addWidget(button2, offset + MAX_HEIGHT * index + 1, 2)

                    button2.hide()

                    self.features_widgets[index * MAX_NUM + shift] = [LONG_REST_TRANSLATE, label, label2, label3, comboBox, button, button2]

                else:
                    label2 = self.features_widgets[index * MAX_NUM + shift][2]
                    comboBox = self.features_widgets[index * MAX_NUM + shift][4]

                    if comboBox.currentText() == HEAVENLY_WINGS:
                        self.flySpeed_label.setText(FLY_SPEED_TRANSLATE + ": %.1f " %self.speed + LENGTH_UNIT)
                        label2.setText(f"+{self.proficiency} {RADIANT}")
                    elif comboBox.currentText() == INNER_RADIANCE:
                        label2.setText(f"+{self.proficiency} {RADIANT}")
                    elif comboBox.currentText() == NECROTIC_SHROUD:
                        label2.setText(f"+{self.proficiency} {NECROTIC}")

            elif feature == BATTLE_MEDIC:
                if changing:
                    label = HoverableLabel(f"{BATTLE_MEDIC} ({HEALERS_KIT})")
                    comboBox = HoverableComboBox()
                    comboBox.addItems([NONE_M, "6", "8", "10", "12"])
                    comboBox.setFixedWidth(100)

                    button = QPushButton(THROW_TRANSLATE, default=False, autoDefault=False)
                    button.setFixedWidth(100)

                    def effect():
                        if comboBox.currentText() != NONE_M:
                            rerolled = False
                            if button.text() != f"1: {REROLL}?":
                                self.update_equipment_quantity(HEALERS_KIT, self.character_info["equipment"][HEALERS_KIT] - 1)
                            else:
                                rerolled = True
                            self.dice(0, f"1d{comboBox.currentText()}")
                            if button.text() == "1" and not rerolled:
                                button.setText(f"1: {REROLL}?")
                        else:
                            button.setText(THROW_TRANSLATE)

                    button.clicked.connect(effect)

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    grid.addWidget(comboBox, offset + MAX_HEIGHT * index + 1, 0)
                    grid.addWidget(button, offset + MAX_HEIGHT * index + 1, 1)

                    self.features_widgets[index * MAX_NUM + shift] = [None, label, comboBox, button]
                else:
                    label = self.features_widgets[index * MAX_NUM + shift][1]
                    comboBox = self.features_widgets[index * MAX_NUM + shift][2]
                    button = self.features_widgets[index * MAX_NUM + shift][3]
                    button.setText(THROW_TRANSLATE)

                if HEALERS_KIT in list(self.character_info["equipment"].keys()):
                    comboBox.show()
                    label.show()
                    button.show()
                else:
                    comboBox.hide()
                    label.hide()
                    button.hide()

            elif feature == LUCK_POINTS:
                if changing:
                    label = HoverableLabel(f"{LUCK_POINTS} ({self.proficiency}/{LONG_REST_TRANSLATE})")
                    if self.character_info["points"][index * MAX_NUM + shift]:
                        button = QPushButton(self.character_info["points"][index * MAX_NUM + shift], default=False, autoDefault=False)
                    else:
                        button = QPushButton(str(self.proficiency), default=False, autoDefault=False)
                    button.setFixedWidth(100)
                    
                    def effect(index, shift):
                        if int(button.text()):
                            self.character_info["points"][index * MAX_NUM + shift] = str(int(button.text()) - 1)
                            button.setText(str(int(button.text()) - 1))

                    button.clicked.connect(lambda _, i=index, s=shift: effect(i, s))

                    grid.addWidget(label, offset + MAX_HEIGHT * index, 0)
                    grid.addWidget(button, offset + MAX_HEIGHT * index + 1, 0)
                    
                    self.features_widgets[index * MAX_NUM + shift] = ["Long Rest PB", label, button]
                else:
                    label = self.features_widgets[index * MAX_NUM + shift][1]

                    label.setText(f"{LUCK_POINTS} ({self.proficiency}/{LONG_REST_TRANSLATE})")

            offset += OFFSET
            shift += 1

    def update_finesse(self, finesse):
        self.character_info["finesse"] = finesse
        
        self.change()


    def update_frightened(self, checked):
        self.character_info[FRIGHTENED] = checked


    def update_gender(self, gender):
        self.character_info["gender"] = gender
        self.update_heightWeight_shown()
        self.update_name_shown()


    def update_gold(self, gold):
        self.character_info["gold"] = gold
        self.change()


    def update_grappled(self, checked):
        self.character_info[GRAPPLED] = checked
        self.update_speed()


    def update_health(self, health):
        self.character_info["health"] = health
        self.update_health_current(self.healthCurrent_lineEdit.text())


    def update_health_current(self, healthCurrent):
        if not self.character_info["health"]:
            healthCurrent = None
            self.healthCurrent_lineEdit.setText("")
        elif int(healthCurrent) > int(self.character_info["health"]):
            healthCurrent = self.character_info["health"]
            self.healthCurrent_lineEdit.setText(healthCurrent)

        if self.character_info["healthCurrent"] != healthCurrent and mainWindow.tabs.currentIndex():
            mainWindow.tabs.setTabText(mainWindow.tabs.currentIndex(), self.character_info["savename"] + "*")
            self.saved = False

        self.healthCurrent_lineEdit.setText(healthCurrent)
        self.character_info["healthCurrent"] = healthCurrent


    def update_health_fixed(self, healthFixed):
        self.character_info["healthFixed"] = healthFixed
        self.update_health_shown()


    def update_health_shown(self): # Updates the health placeholder (possible health if no health is inputed)
        level = self.character_info["level"]
        classe = self.character_info["class"]
        healthBase = self.species_healthBase + self.lineage_healthBase
        self.healthBonus = self.species_healthBonus + self.lineage_healthBonus

        if self.character_info["age"]:
            age = self.character_info["age"]
        else:
            age = self.age_lineEdit.placeholderText()

        if classe != RANDOM and level != RANDOM and level != "0" and self.hitDie:
            if not self.character_info["healthFixed"]:
                result, _, _, _ = self.throw(0, f"{int(level) - 1}d{self.hitDie}")
                health = self.hitDie + self.ability_bonuses[2] + self.healthBonus + (int(level) - 1) * (self.ability_bonuses[2] + self.healthBonus) + result
            else:
                health = self.hitDie + self.ability_bonuses[2] + self.healthBonus + (int(level) - 1) * (self.ability_bonuses[2] + self.hitDieFixed + self.healthBonus)
        elif age and int(age) < self.developmentAge: 
            health = int(healthBase * int(age)/self.developmentAge) # int just to make sure it's not a float
        elif level == "0":
            health = healthBase
        else:
            health = ""

        if health:
            self.health_lineEdit.setPlaceholderText(str(int(health)))
        else:
            self.health_lineEdit.setPlaceholderText("")
        
        self.change()


    def update_height(self, height):
        self.character_info["height"] = height

        self.change()


    def update_heightWeight_shown(self):
        if self.character_info["age"]:
            age = int(self.character_info["age"])
        else:
            age = self.age_lineEdit.placeholderText()

        height_modifier = self.lineage_height_modifier + self.species_height_modifier
        height = self.lineage_height_base + self.species_height_base + height_modifier / 12
        weight = self.lineage_weight_base + self.species_weight_base + height_modifier * (self.lineage_weight_modifier + self.species_weight_modifier)

        if self.developmentAge and age and int(age) < self.developmentAge:
            height = height / 2 + height * (int(age)/self.developmentAge) / 2
            weight = weight / 2 + weight * (int(age)/self.developmentAge) / 2

        if self.character_info["gender"] == GENDERS[1]:
            height = height * HEIGHT_GENDER
            weight = weight * WEIGHT_GENDER
        elif not self.character_info["gender"] == GENDERS[2]:
            height = 0
            weight = 0

        if height:
            self.height_lineEdit.setPlaceholderText("%.2f " %(height))
        else:
            self.height_lineEdit.setPlaceholderText("")
        
        if weight:
            self.weight_lineEdit.setPlaceholderText("%.2f " %(weight))
        else:
            self.weight_lineEdit.setPlaceholderText("")

        self.change()

    
    def update_hit_die(self, hitDie):
        if int(hitDie) >= int(self.character_info["level"]):
            hitDie = self.character_info["level"]
        self.hitDie_lineEdit.setText(hitDie)
        self.character_info["hitDie"] = hitDie


    def update_incapacitated(self, checked):
        self.character_info[INCAPACITATED] = checked
        if checked:
            self.character_info["incapacitated counter"] += 1
        else:
            self.character_info["incapacitated counter"] -= 1
        if self.character_info["incapacitated counter"]:
            self.incapacitated_checkBox.setChecked(True)
        else:
            self.incapacitated_checkBox.setChecked(False)

        for i in range(LEN_FEATURES):
            self.update_features(i, False)


    def update_invisible(self, checked):
        self.character_info[INVISIBLE] = checked


    def update_languages(self, newRace = True):
        old = self.languages
        self.languages = self.species_languages + self.lineage_languages

        new = self.languages
        widgets = self.languages_widgets
        chosen = [] # Currently chosen elements
        grid = self.languages_grid
        available = self.character_info["available languages"].copy()

        # Check already existing widgets to see if they need to be deleted
        for i, widget in enumerate(widgets):
            if widget and (i >= len(new) or i >= len(old)):
                widget.hide()
                widget.deleteLater()
                widgets[i] = None
                chosen.append("")
            else:
                if widget and widget.currentText() != RANDOM and widget.currentText() != RANDOM_STANDARD and widget.currentText() != RANDOM_RARE and (i >= len(old) or old[i] != widget.currentText()) and not newRace:
                    chosen.append(widget.currentText())
                elif widget and widget.currentText() == RANDOM and (i >= len(old) or old[i] != RANDOM and not newRace):
                    chosen.append("rng")
                elif widget and widget.currentText() == RANDOM_STANDARD and (i >= len(old) or old[i] != RANDOM_STANDARD and not newRace):
                    chosen.append("rng standard")
                elif widget and widget.currentText() == RANDOM_RARE and (i >= len(old) or old[i] != RANDOM_RARE and not newRace):
                    chosen.append("rng rare")
                else:
                    chosen.append("")

        for i, widget in enumerate(widgets):
            if not widget:
                widgets.pop(i)

        for i, element in enumerate(new):
            elements = [choice for choice in [RANDOM, RANDOM_STANDARD, RANDOM_RARE] + STANDARD_LANGUAGES + RARE_LANGUAGES if choice not in chosen] # Remove elements that are already in another comboBox
            if len(widgets) < i + 1:
                comboBox = HoverableComboBox()
                grid.addWidget(comboBox, i, 0)
                comboBox.activated.connect(lambda: self.update_languages(False))
                widgets.append(comboBox)
                comboBox.addItems(elements)
            
            text = widgets[i].currentText()
            elements = [choice for choice in available if choice not in chosen or (choice == text and choice in self.character_info["available languages"])] # Remove elements that are already in another comboBox
            widgets[i].clear()
            widgets[i].addItems(elements)
            widgets[i].setCurrentText(text)

            if len(chosen) > i and chosen[i]:
                widgets[i].setCurrentText(chosen[i])
                if chosen[i] == "rng":
                    widgets[i].setCurrentText(RANDOM)
                if chosen[i] == "rng standard":
                    widgets[i].setCurrentText(RANDOM_STANDARD)
                if chosen[i] == "rng rare":
                    widgets[i].setCurrentText(RANDOM_RARE)
            
            elif element not in chosen or element == RANDOM or element == RANDOM_STANDARD or element == RANDOM_RARE:
                if element in elements:
                    widgets[i].setCurrentText(element)
                    if len(chosen) > i:
                        chosen[i] = element
                    else:
                        chosen.append(element)
                elif element:
                    if len(chosen) > i:
                        chosen[i] = "rng"
                    else:
                        chosen.append("rng")

        for i, choice in enumerate(chosen):
            if choice == "rng":
                chosen[i] = RANDOM
            if choice == "rng standard":
                chosen[i] = RANDOM_STANDARD
            if choice == "rng rare":
                chosen[i] = RANDOM_RARE
        self.languages_widgets = widgets
        self.character_info["languages"] = chosen


    def update_level(self, level):
        self.character_info["level"] = level

        if level and level != RANDOM and int(level):
            self.hitDie_lineEdit.show()
            self.hitDie_label.show()
            self.hitDie_button.show()
            self.hitDie_lineEdit.setText(level)
            self.hitDie_label.setText(f"{HIT_DICE_TRANSLATE} ({level}d{self.hitDie}):")
            
        else:
            self.hitDie_lineEdit.hide()
            self.hitDie_label.hide()
            self.hitDie_button.hide()
            self.character_info["hitDie"] = None

        if level != RANDOM:

            self.proficiency = int(np.ceil(int(level)/4 + 1))
            if int(level) >= 4:
                self.feats_widgets[2]["label"].show()
                self.feats_widgets[2]["comboBox"].show()
            else:
                self.feats_widgets[2]["label"].hide()
                self.feats_widgets[2]["comboBox"].hide()
                self.feats_widgets[2]["comboBox"].setCurrentIndex(0)
            if int(level) >= 8:
                self.feats_widgets[3]["label"].show()
                self.feats_widgets[3]["comboBox"].show()
            else:
                self.feats_widgets[3]["label"].hide()
                self.feats_widgets[3]["comboBox"].hide()
                self.feats_widgets[3]["comboBox"].setCurrentIndex(0)
            if int(level) >= 12:
                self.feats_widgets[4]["label"].show()
                self.feats_widgets[4]["comboBox"].show()
            else:
                self.feats_widgets[4]["label"].hide()
                self.feats_widgets[4]["comboBox"].hide()
                self.feats_widgets[4]["comboBox"].setCurrentIndex(0)
            if int(level) >= 16:
                self.feats_widgets[5]["label"].show()
                self.feats_widgets[5]["comboBox"].show()
            else:
                self.feats_widgets[5]["label"].hide()
                self.feats_widgets[5]["comboBox"].hide()
                self.feats_widgets[5]["comboBox"].setCurrentIndex(0)
            if int(level) >= 19:
                self.feats_widgets[6]["label"].show()
                self.feats_widgets[6]["comboBox"].show()
            else:
                self.feats_widgets[6]["label"].hide()
                self.feats_widgets[6]["comboBox"].hide()
                self.feats_widgets[6]["comboBox"].setCurrentIndex(0)
        else:
            self.proficiency = 1
            for i in range(2, LEN_FEATS):
                self.feats_widgets[i]["label"].hide()
                self.feats_widgets[i]["comboBox"].hide()
        
        self.ProficiencyBonus_label.setText(f"{PROFICIENCIES_BONUS_TRANSLATE}: {self.proficiency}")
        for i in range(2, LEN_FEATS):
            self.update_feat(i)
            
        for i in range(LEN_FEATURES):
            self.update_features(i, False)

        self.update_misc()


    def update_lineage(self, lineage):
        self.character_info["lineage"] = lineage
        species = self.character_info["species"]

        self.lineage_size = SPECIES[species]["lineages"][lineage].get("size", "")
        self.update_size()

        self.lineage_weapon_proficiencies = SPECIES[species]["lineages"][lineage].get("weapon proficiencies", [])
        self.lineage_armor_proficiencies = SPECIES[species]["lineages"][lineage].get("armor proficiencies", [])
        self.lineage_tool_proficiencies = SPECIES[species]["lineages"][lineage].get("tool proficiencies", [])
        self.update_proficiencies()

        self.lineage_skill_proficiencies = SPECIES[species]["lineages"][lineage].get("skill proficiencies", [])
        self.update_skill_proficiencies()

        self.lineage_condition_advantages = SPECIES[species]["lineages"][lineage].get("condition advantages", [])
        self.lineage_condition_immunities = SPECIES[species]["lineages"][lineage].get("condition immunities", [])
        self.lineage_save_advantages = SPECIES[species]["lineages"][lineage].get("save advantages", [])
        self.lineage_resistances = SPECIES[species]["lineages"][lineage].get("resistances", [])
        self.update_resistances()

        self.lineage_misc = SPECIES[species]["lineages"][lineage].get("misc", [])
        self.update_misc()

        self.lineage_magic = SPECIES[species]["lineages"][lineage].get("magic", [])
        self.update_magic()

        self.lineage_ageMax = SPECIES[species]["lineages"][lineage].get("age max", 0)
        self.lineage_height_base = SPECIES[species]["lineages"][lineage].get("base height", 0) * LENGTH_MULTIPLIER
        self.lineage_weight_base = SPECIES[species]["lineages"][lineage].get("base weight", 0) * WEIGHT_MULTIPLIER
        result, _, _, _ = self.throw(0, SPECIES[species]["lineages"][lineage].get("height modifier", "0"))
        self.lineage_height_modifier = result * LENGTH_MULTIPLIER
        result, _, _, _ = self.throw(0, SPECIES[species]["lineages"][lineage].get("weight modifier", "0"))
        self.lineage_weight_modifier = result * WEIGHT_MULTIPLIER
        self.update_ageMinMax_shown()

        self.lineage_speed = SPECIES[species]["lineages"][lineage].get("speed", 0) * LENGTH_MULTIPLIER
        self.lineage_darkvision = SPECIES[species]["lineages"][lineage].get("darkvision", 0) * LENGTH_MULTIPLIER
        self.update_speed()
        self.update_darkvision()

        self.lineage_namesMale = SPECIES[species]["lineages"][lineage].get("male names", [])
        self.lineage_namesFemale = SPECIES[species]["lineages"][lineage].get("female names", [])
        self.lineage_namesLast = SPECIES[species]["lineages"][lineage].get("last names", [])
        self.update_name_shown()

        self.lineage_healthBase = SPECIES[species]["lineages"][lineage].get("health base", 0)
        self.lineage_healthBonus = SPECIES[species]["lineages"][lineage].get("health bonus", 0)
        self.update_health_shown()

        self.lineage_languages = SPECIES[species]["lineages"][lineage].get("languages", [])

        self.update_languages()

    
    def update_magic(self):
        spells = self.species_magic + self.lineage_magic + self.class_magic + self.subclass_magic
        for widgets in self.spell_widgets:
            for widget in widgets:
                widget.hide()
                widget.deleteLater()

        self.spell_widgets = [[]] * 10

        for magic in spells:
            spell = magic[0]
            level = SPELLS[spell]["level"]

            label = HoverableLabel(spell)
            label.setFixedHeight(int(scale_factor * 20))
            self.spell_widgets[level].append(label)
            self.spell_vBoxes[level].addWidget(label)

        self.change()


    def update_main_hand(self, weapon, state):
        if state:
            self.character_info["mainHand"] = weapon
            for widgets in self.equipment_widgets:
                if widgets:
                    element = widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "")
                    if element != weapon and element not in ARMORS:
                        widgets["checkBox2"].setEnabled(False)
                    else:
                        widgets["button0"].show()
                        widgets["button1"].show()
            if weapon in WEAPONS and LIGHT in WEAPONS[weapon]["properties"]:
                for widgets in self.equipment_widgets:
                    if widgets and not self.character_info["offHand"]:
                        element = widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "")
                        if (weapon != element and element in WEAPONS and LIGHT in WEAPONS[element]["properties"]) or (weapon == element and self.character_info["equipment"][weapon] > 1) or element in WEAPON_TYPES[SHIELDS_TRANSLATE]:
                            widgets["checkBox1"].setEnabled(True)
            else:
                for widgets in self.equipment_widgets:
                    if widgets:
                        widgets["checkBox1"].setEnabled(False)
        else:
            self.character_info["mainHand"] = None
            for widgets in self.equipment_widgets:
                if widgets:
                    if self.character_info["offHand"] not in WEAPON_TYPES[SHIELDS_TRANSLATE]: # If the off hand is not a shield, it will be removed later in this method, so no need to worry about it
                        widgets["checkBox2"].setEnabled(True)
                    element = widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "")
                    if element not in WEAPON_TYPES[SHIELDS_TRANSLATE]: # The idea is that shields can be carried in the offhand by themselves, while weapons need to be carried in the main hand if they are the only weapon
                        widgets["checkBox1"].setEnabled(False)
                    else:
                        widgets["checkBox1"].setEnabled(True)
                    if weapon == element:
                        widgets["button0"].hide()
                        widgets["button1"].hide()
                        widgets["button2"].hide()
                    if isinstance(widgets["checkBox1"], QCheckBox) and widgets["checkBox1"].isChecked() and not widgets["checkBox1"].isEnabled():
                        widgets["checkBox1"].setChecked(False)
                        self.character_info["offHand"] = None


    def update_misc(self):
        misc = self.species_misc + self.lineage_misc + self.class_misc + self.subclass_misc
        for element in self.feats_misc:
            misc += element
        for i, element in enumerate(misc):
            if isinstance (element, list):
                if self.character_info["level"] != RANDOM and int(self.character_info["level"]) >= element[0]:
                    misc[i] = element[2]
                else:
                    misc[i] = f"{element[1]} ({LEVEL_TRANSLATE.lower()} {element[0]})"
        self.misc_label.setText("\n\n".join(misc))

        self.change()


    def update_name(self, name):
        self.character_info["name"] = name

        self.change()


    def update_name_shown(self):
        namesMale = self.species_namesMale + self.lineage_namesMale
        namesFemale = self.species_namesFemale + self.lineage_namesFemale
        namesLast = self.species_namesLast + self.lineage_namesLast

        if self.character_info["gender"] == GENDERS[1]:
            names = namesMale
        elif self.character_info["gender"] == GENDERS[2]:
            names = namesFemale
        else:
            names = ""

        if names and namesLast:
            name = f"{random.choice(names)} {random.choice(namesLast)}"
        elif names:
            name = random.choice(names)
        else:
            name = ""

        self.name_lineEdit.setPlaceholderText(name)

        self.change()


    def update_off_hand(self, weapon, state):
        if state:
            self.character_info["offHand"] = weapon
            for widgets in self.equipment_widgets:
                if widgets:
                    if widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "") != weapon:
                        widgets["checkBox1"].setEnabled(False)
                    else:
                        widgets["button2"].show()
                        widgets["button0"].show()
        else:
            self.character_info["offHand"] = None
            for widgets in self.equipment_widgets:
                if widgets:
                    element = widgets["label"].text().replace("<u>", "").replace("</u>", "").replace("*", "")
                    if weapon == element:
                        widgets["button2"].hide()
                        if not widgets["checkBox2"].isChecked():
                            widgets["button0"].hide()
                    if (element in WEAPONS and LIGHT in WEAPONS[element]["properties"] and (not widgets["checkBox2"].isChecked() or self.character_info["equipment"][element] > 1)) or element in WEAPON_TYPES[SHIELDS_TRANSLATE]:
                        widgets["checkBox1"].setEnabled(True)


    def update_paralyzed(self, checked):
        self.character_info[PARALYZED] = checked
        if checked:
            self.update_incapacitated(True)
            self.abilities_widgets[STRENGTH]["button"].setEnabled(False)
            self.abilities_widgets[DEXTERITY]["button"].setEnabled(False)
            self.abilities_widgets[STRENGTH]["button"].setText(THROW_TRANSLATE)
            self.abilities_widgets[DEXTERITY]["button"].setText(THROW_TRANSLATE)
        else:
            self.update_incapacitated(False)
            if not self.character_info[PETRIFIED] and not self.character_info[UNCONSCIOUS]:
                self.abilities_widgets[STRENGTH]["button"].setEnabled(True)
                self.abilities_widgets[DEXTERITY]["button"].setEnabled(True)



    def update_petrified(self, checked):
        self.character_info[PETRIFIED] = checked
        weight = self.weight_lineEdit.text()
        if checked:
            weight = weight * 10
            self.update_incapacitated(True)
            self.abilities_widgets[STRENGTH]["button"].setEnabled(False)
            self.abilities_widgets[DEXTERITY]["button"].setEnabled(False)
            self.abilities_widgets[STRENGTH]["button"].setText(THROW_TRANSLATE)
            self.abilities_widgets[DEXTERITY]["button"].setText(THROW_TRANSLATE)
        else:
            weight = weight / 10
            self.update_incapacitated(False)
            if not self.character_info[UNCONSCIOUS] and not self.character_info[PARALYZED]:
                self.abilities_widgets[STRENGTH]["button"].setEnabled(True)
                self.abilities_widgets[DEXTERITY]["button"].setEnabled(True)
        self.update_weight(weight)
        self.update_resistances()


    def update_platinum(self, platinum):
        self.character_info["platinum"] = platinum
        self.change()


    def update_poisoned(self, checked):
        self.character_info[POISONED] = checked

    
    def update_proficiencies(self):
        self.weapon_proficiencies = self.species_weapon_proficiencies + self.lineage_weapon_proficiencies + self.class_weapon_proficiencies + self.subclass_weapon_proficiencies
        weapon_list = []
        weapon_elements = []
        for weapon in self.weapon_proficiencies:
            if isinstance(weapon, list):
                weapon_list.append(weapon)
            else:
                weapon_elements.append(weapon)

        armor_proficiencies = self.species_armor_proficiencies + self.lineage_armor_proficiencies + self.class_armor_proficiencies + self.subclass_armor_proficiencies
        armor_list = []
        armor_elements = []
        for armor in armor_proficiencies:
            if isinstance(armor, list):
                armor_list.append(armor)
            else:
                armor_elements.append(armor)

        tool_proficiencies = self.species_tool_proficiencies + self.lineage_tool_proficiencies + self.class_tool_proficiencies + self.subclass_tool_proficiencies + self.background_tool_proficiencies
        for i in range(LEN_FEATS):
            tool_proficiencies += self.feats_tool_proficiencies[i]

        tool_list = []
        tool_elements = []
        for tool in tool_proficiencies:
            if isinstance(tool, list):
                tool_list.append(tool)
            else:
                tool_elements.append(tool)

        proficiencies = sorted(list(dict.fromkeys(weapon_elements))) + weapon_list
        if armor_proficiencies:
            proficiencies += ["\n"] + sorted(list(dict.fromkeys(armor_elements))) + armor_list
        if tool_proficiencies:
            proficiencies += ["\n"] + sorted(list(dict.fromkeys(tool_elements))) + tool_list

        if len(proficiencies) and proficiencies[0] == "\n":
            proficiencies.pop(0)

        old = self.proficiencies
        new = proficiencies
        widgets = self.proficiencies_widgets
        chosen = [] # Current chosen elements
        chosenOrdered = []
        grid = self.proficiencies_grid

        # Check already existing widgets to see if they need to be deleted    
        for i, widget in enumerate(widgets):
            if widget and (i >= len(new) or i >= len(old) or new[i] != old[i]):
                widget.hide()
                widget.deleteLater()
                widgets[i] = None
            else:
                if isinstance(widget, HoverableComboBox) and widget.currentText() != RANDOM:
                    chosen.append(widget.currentText())
                elif isinstance(widget, HoverableLabel):
                    chosen.append(widget.text())

        for i, widget in enumerate(widgets):
            if not widget:
                widgets.pop(i)

        for i, element in enumerate(new):
            if isinstance(element, list):
                if not isinstance(element[0], list):
                    element = list(dict.fromkeys(element)) # Remove duplicates that can come from species, lineage and class
                    elements = [choice for choice in element if choice not in chosen] # Remove elements that are already in another comboBox
                    
                    if len(widgets) < i + 1 or not widgets[i]:
                        comboBox = HoverableComboBox()
                        grid.addWidget(comboBox, i, 0)
                        comboBox.activated.connect(self.update_proficiencies)
                        if len(widgets) < i + 1:
                            widgets.append(comboBox)
                        else:
                            widgets[i] = comboBox
                        comboBox.addItems(elements)

                    text = widgets[i].currentText()
                    elements = [choice for choice in element if choice not in chosen or choice == text] # Remove elements that are already in another comboBox
                    widgets[i].clear()
                    widgets[i].addItems(elements)
                    widgets[i].setCurrentText(text)
                else:
                    element = element[0]
                    elements = [choice for choice in element if choice not in chosen]
                    
                    if len(widgets) < i + 1 or not widgets[i]:
                        self.extra_comboBox = HoverableComboBox()
                        grid.addWidget(self.extra_comboBox, i, 0)
                        self.extra_comboBox.activated.connect(self.update_proficiencies)
                        self.extra_comboBox.activated.connect(lambda: self.update_equipment_extra(self.extra_comboBox.currentText()))
                        if len(widgets) < i + 1:
                            widgets.append(self.extra_comboBox)
                        else:
                            widgets[i] = self.extra_comboBox
                        self.extra_comboBox.addItems(elements)

                    text = widgets[i].currentText()
                    elements = [choice for choice in element if choice not in chosen or choice == text] # Remove elements that are already in another comboBox
                    widgets[i].clear()
                    widgets[i].addItems(elements)
                    widgets[i].setCurrentText(text)
            elif element not in chosen or element == "\n":
                if len(old) < i + 1 or element != old[i]:
                    label = HoverableLabel(element)
                    grid.addWidget(label, i, 0)
                    if len(widgets) < i + 1:
                        widgets.append(label)
                    else:  
                        widgets[i] = label
                chosen.append(element)

        for i, widget in enumerate(widgets):
            if isinstance(widget, HoverableComboBox):
                chosenOrdered.append(widget.currentText())

        self.proficiencies = new
        self.proficiencies_widgets = widgets
        self.character_info["proficiencies"] = chosenOrdered

        for i in range(LEN_FEATURES):
            self.update_features(i, False)

        self.change()


    def update_prone(self, checked):
        self.character_info[PRONE] = checked


    def update_resistances(self):
        condition_advantages = self.species_condition_advantages + self.lineage_condition_advantages + self.class_condition_advantages + self.subclass_condition_advantages
        condition_immunities = self.species_condition_immunities + self.lineage_condition_immunities + self.class_condition_immunities + self.subclass_condition_immunities
        save_advantages = self.species_save_advantages + self.lineage_save_advantages + self.class_save_advantages + self.subclass_save_advantages
        throw_advantages = []
        throw_disadvantages = self.armor_disadvantages
        resistances = self.species_resistances + self.lineage_resistances + self.class_resistances + self.subclass_resistances

        for features_resistances in self.features_resistances:
            for resistance in features_resistances:
                if resistance:
                    resistances.append(resistance)

        for features_save_advantages in self.features_save_advantages:
            for save_advantage in features_save_advantages:
                if save_advantage:
                    save_advantages.append(save_advantage)

        for features_throw_advantages in self.features_throw_advantages:
            for throw_advantage in features_throw_advantages:
                if throw_advantage:
                    throw_advantages.append(throw_advantage)

        if self.character_info[PETRIFIED]:
            resistances += [ALL_DAMAGE]
            condition_immunities += [POISONED]

        save_disadvantages = []
        if self.character_info[STUNNED]:
            save_disadvantages += [STRENGTH, DEXTERITY]
        elif self.character_info[RESTRAINED]:
            save_disadvantages += [DEXTERITY]

        # Remove duplicates by converting to set and back to list
        resistances = list(set(resistances))
        condition_immunities = list(set(condition_immunities))
        condition_advantages = list(set(condition_advantages))
        save_advantages = list(set(save_advantages))
        save_disadvantages = list(set(save_disadvantages))
        throw_advantages = list(set(throw_advantages))
        throw_disadvantages = list(set(throw_disadvantages))

        conditions = ""
        if resistances:
            conditions += f"{RESISTENT_TRANSLATE}\n{"\n".join(resistances)}\n\n"
        if condition_immunities:
            conditions += f"{IMMUNE_TRANSLATE}\n{"\n".join(condition_immunities)}\n\n"
        if condition_advantages:
            conditions += f"{ADVANTAGE_AGAINST_TRANSLATE}\n{"\n".join(condition_advantages)}\n\n"
        if throw_advantages:
            conditions += f"{SKILL_ADVANTAGE_TRANSLATE}\n{"\n".join(throw_advantages)}\n\n"
        if throw_disadvantages:
            conditions += f"{SKILL_DISADVANTAGE_TRANSLATE}\n{"\n".join(throw_disadvantages)}\n\n"
        if save_advantages:
            conditions += f"{SAVE_ADVANTAGE_TRANSLATE}\n{"\n".join(save_advantages)}\n\n"
        if save_disadvantages:
            conditions += f"{SAVE_DISADVANTAGE_TRANSLATE}\n{"\n".join(save_disadvantages)}"

        self.resistances_label.setText(conditions.rstrip())

        self.change()


    def update_restrained(self, checked):
        self.character_info[RESTRAINED] = checked
        self.update_resistances()


    def update_silver(self, silver):
        self.character_info["silver"] = silver
        self.change()


    def update_size(self):
        size = self.species_size + self.lineage_size
        self.size_label.setText(f"{SIZE_TRANSLATE}: {size}")
    

    def update_skill_proficiencies(self):
        skill_proficiencies = self.species_skill_proficiencies + self.lineage_skill_proficiencies + self.class_skill_proficiencies + self.subclass_skill_proficiencies + self.background_skill_proficiencies
        
        self.skill_proficiencies_choices = []
        self.skill_proficiencies_fixed = []

        for skill in skill_proficiencies:
            if isinstance(skill, list):
                self.skill_proficiencies_choices.append(skill)
            else:
                self.skill_proficiencies_fixed.append(skill)
                self.skills_widgets[skill]["checkBox"].setChecked(True)

        self.update_skills() # Determining which skills can be checked


    def update_skills(self, skillEdit = None):
        choices = self.skill_proficiencies_choices
        fixed = self.skill_proficiencies_fixed

        if skillEdit:
            self.character_info[skillEdit] = self.skills_widgets[skillEdit]["checkBox"].isChecked()
        for skill in SKILLS:
            self.skills_widgets[skill]["checkable"].clear()
            self.skills_widgets[skill]["checkBox"].setEnabled(False)

        checked = [[] for _ in range(19)]

        for i, choice in enumerate(choices):
            for j in range(18):
                checked[j] = checked[j] + [0]
            for j in range(len(choice)):
                skill = choice[j]
                if skill not in fixed:
                    self.skills_widgets[skill]["checkable"].append(i)
                    self.skills_widgets[skill]["checkBox"].setEnabled(True)
                    
                    if self.skills_widgets[skill]["checkBox"].isChecked():
                        checked[list(SKILLS.keys()).index(skill)][i] = 1

        checked = [i for i in checked if i != [0] * len(i)]
        checked = np.sum(checked, 0)
        order = np.argsort(checked)

        if isinstance(checked, np.ndarray):
            checkedNonVoid = [i for i in checked if i != 0]
            checked = np.sort(checked)

            for j in range(checked[-1]):
                for i in range(len(checked)):
                    if len(checkedNonVoid) == checked[-1] and checked[i] != 0:
                        for skill in choices[order[i]]:
                            if order[i] in self.skills_widgets[skill]["checkable"]:
                                self.skills_widgets[skill]["checkable"].remove(order[i])
                                if not self.skills_widgets[skill]["checkable"] and not self.skills_widgets[skill]["checkBox"].isChecked():
                                    self.skills_widgets[skill]["checkBox"].setEnabled(False)
                    if len(checkedNonVoid) < checked[-1]: # Number of available choices has decreased
                        for skill in reversed(choices[order[np.argmax(checked)]]):
                            if order[np.argmax(checked)] in self.skills_widgets[skill]["checkable"] and self.skills_widgets[skill]["checkBox"].isChecked():
                                self.skills_widgets[skill]["checkBox"].setChecked(False)
                                break
                        break
                checked = checked - 1
                checkedNonVoid = [i for i in checked if i!= 0]

        for skill in SKILLS:
            if not self.skills_widgets[skill]["checkBox"].isEnabled() and self.skills_widgets[skill]["checkBox"].isChecked() and skill not in fixed:
                self.skills_widgets[skill]["checkBox"].setChecked(False)
                self.character_info[skill] = False
            elif skill in fixed:
                self.character_info[skill] = False

        self.update_skills_shown()


    def update_skills_shown(self):
        for skill in SKILLS:        
            if self.skills_widgets[skill]["checkBox"].isChecked():
                self.skills_widgets[skill]["label"].setText(f"{skill}: {self.ability_bonuses[ABILITIES.index(SKILLS[skill])] + self.proficiency}")
            else:
                self.skills_widgets[skill]["label"].setText(f"{skill}: {self.ability_bonuses[ABILITIES.index(SKILLS[skill])]}")

        self.passivePerception_label.setText(f"{PASSIVE_PERCEPTION_TRANSLATE}: {10 + int(self.skills_widgets[PERCEPTION]["label"].text().split(": ")[1])}")

        self.change()
        

    def update_species(self, species):
        self.character_info["species"] = species

        self.species_size = SPECIES[species].get("size", "")

        lineage = self.lineage_comboBox.currentText()
        self.lineage_comboBox.clear()
        self.lineage_comboBox.addItems(SPECIES[species]["lineages"])
        self.lineage_comboBox.setCurrentText(lineage)

        if len(SPECIES[species]["lineages"]) != 1:
            self.lineage_comboBox.show()
            self.lineage_label.show()
        else:
            self.lineage_comboBox.hide()
            self.lineage_label.hide()

        self.species_weapon_proficiencies = SPECIES[species].get("weapon proficiencies", [])
        self.species_armor_proficiencies =SPECIES[species].get("armor proficiencies", [])
        self.species_tool_proficiencies = SPECIES[species].get("tool proficiencies", [])

        self.species_skill_proficiencies = SPECIES[species].get("skill proficiencies", [])

        self.species_condition_advantages = SPECIES[species].get("condition advantages", [])
        self.species_condition_immunities = SPECIES[species].get("condition immunities", [])
        self.species_save_advantages = SPECIES[species].get("save advantages", [])
        self.species_resistances = SPECIES[species].get("resistances", [])

        self.species_misc = SPECIES[species].get("misc", [])

        self.species_magic = SPECIES[species].get("magic", [])

        self.developmentAge = SPECIES[species].get("development age", 0) # Used to check if the character is as tall as an adult
        
        self.species_ageMax = SPECIES[species].get("age max", 0)
        self.species_height_base = SPECIES[species].get("base height", 0) * LENGTH_MULTIPLIER
        self.species_weight_base = SPECIES[species].get("base weight", 0) * WEIGHT_MULTIPLIER
        result, _, _, _ = self.throw(0, SPECIES[species].get("height modifier", "0"))
        self.species_height_modifier = result * LENGTH_MULTIPLIER
        result, _, _, _ = self.throw(0, SPECIES[species].get("weight modifier", "0"))
        self.species_weight_modifier = result * WEIGHT_MULTIPLIER

        self.species_speed = SPECIES[species].get("speed", 0) * LENGTH_MULTIPLIER
        self.species_darkvision = SPECIES[species].get("darkvision", 0) * LENGTH_MULTIPLIER

        self.species_namesMale = SPECIES[species].get("male names", [])
        self.species_namesFemale = SPECIES[species].get("female names", [])
        self.species_namesLast = SPECIES[species].get("last names", [])

        self.species_healthBase = SPECIES[species].get("health base", 0)
        self.species_healthBonus = SPECIES[species].get("health bonus", 0)

        self.species_languages = SPECIES[species].get("languages", [])

        self.update_lineage(self.lineage_comboBox.currentText())

        if "feat" in SPECIES[species]:
            self.feats_widgets[1]["comboBox"].show()
            self.feats_widgets[1]["label"].show()
            feat = self.feats_widgets[1]["comboBox"].currentText()
            self.feats_widgets[1]["comboBox"].clear()
            self.feats_widgets[1]["comboBox"].addItems(SPECIES[species]["feat"])
            self.feats_widgets[1]["comboBox"].setCurrentText(feat)
        else:
            self.feats_widgets[1]["comboBox"].hide()
            self.feats_widgets[1]["label"].hide()
            self.feats_widgets[1]["comboBox"].clear()

        self.features[LEN_FEATS] = SPECIES[species].get("features", [""])
        self.update_features(LEN_FEATS)


    def update_speed(self):
        self.speed = self.species_speed + self.lineage_speed + self.armor_speed
        if self.speed or self.speed == 0:
            if self.character_info[GRAPPLED] or self.character_info[PARALYZED] or self.character_info[PETRIFIED] or self.character_info[RESTRAINED] or self.character_info[UNCONSCIOUS]:
                self.speed = 0
            elif self.character_info[EXHAUSTION]:
                self.speed = self.speed - 5 * self.character_info[EXHAUSTION]
            if self.speed < 0:
                self.speed = 0
            self.speed = self.speed * LENGTH_MULTIPLIER
            self.speed_label.setText(f"{SPEED_TRANSLATE}: {self.speed:.1f}{LENGTH_UNIT}")
        else:
            self.speed_label.setText(f"{SPEED_TRANSLATE}:")

        self.change()


    def update_stunned(self, checked):
        self.character_info[STUNNED] = checked
        if checked:
            self.update_incapacitated(True)
        else:
            self.update_incapacitated(False)
        self.update_resistances()


    def update_subclass(self, subclass):
        self.character_info["subclass"] = subclass
        classe = self.character_info["class"]

        self.subclass_weapon_proficiencies = CLASSES[classe]["subclasses"][subclass].get("weapon proficiencies", [])
        self.subclass_armor_proficiencies = CLASSES[classe]["subclasses"][subclass].get("armor proficiencies", [])
        self.subclass_tool_proficiencies = CLASSES[classe]["subclasses"][subclass].get("tool proficiencies", [])
        self.update_proficiencies()

        self.subclass_skill_proficiencies = CLASSES[classe]["subclasses"][subclass].get("skill proficiencies", [])
        self.update_skill_proficiencies()

        self.subclass_condition_advantages = CLASSES[classe]["subclasses"][subclass].get("condition advantages", [])
        self.subclass_condition_immunities = CLASSES[classe]["subclasses"][subclass].get("condition immunities", [])
        self.subclass_save_advantages = CLASSES[classe]["subclasses"][subclass].get("save advantages", [])
        self.subclass_resistances = CLASSES[classe]["subclasses"][subclass].get("resistances", [])
        self.update_resistances()

        self.subclass_misc = CLASSES[classe]["subclasses"][subclass].get("misc", [])
        self.update_misc()

        self.subclass_magic = CLASSES[classe]["subclasses"][subclass].get("magic", [])
        self.update_magic()


    def update_unconscious(self, checked):
        self.character_info[UNCONSCIOUS] = checked
        if checked:
            self.update_incapacitated(True)
            self.prone_checkBox.setChecked(True)
            self.update_prone(True)
            self.abilities_widgets[STRENGTH]["button"].setEnabled(False)
            self.abilities_widgets[DEXTERITY]["button"].setEnabled(False)
            self.abilities_widgets[STRENGTH]["button"].setText(THROW_TRANSLATE)
            self.abilities_widgets[DEXTERITY]["button"].setText(THROW_TRANSLATE)
        else:
            self.update_incapacitated(False)
            self.update_prone(False)
            if not self.character_info[PETRIFIED] and not self.character_info[PARALYZED]:
                self.abilities_widgets[STRENGTH]["button"].setEnabled(True)
                self.abilities_widgets[DEXTERITY]["button"].setEnabled(True)


    def update_weight(self, weight):
        self.character_info["weight"] = weight

        self.change()
            

class MyApp(QDialog):
    def __init__(self):
        super().__init__()

        # Initialize tab screen
        self.tabs = QTabWidget()
        self.tabs.setTabsClosable(True)
        self.tabs.tabCloseRequested.connect(self.close_tab)

        self.setWindowTitle(GENERATOR_WINDOW)
        self.setWindowFlag(Qt.WindowType.WindowMinimizeButtonHint, True)
        self.setWindowFlag(Qt.WindowType.WindowMaximizeButtonHint, True)
        
        # Here I start by placing the general layout of the window
        layout = QVBoxLayout(self)

        layout.addWidget(self.tabs)
        self.setLayout(layout)

        self.setStyleSheet("""
            QWidget {
                background-color: #3a3a3a;
            }
            QCheckBox::indicator:unchecked:disabled{
                border: 1px solid #2d2d2d;
                border-radius: 5px;
                background-color: #2d2d2d;
            }
            HoverableComboBox::drop-down {
                image: none;
            }
            QLineEdit {
                border: 1px solid #d3d3d3;
                border-radius: 5px;
                height: 20px;
            }
            HoverableComboBox {
                border: 1px solid #d3d3d3;
                border-radius: 5px;
                height: 20px;
            }
            QPushButton {
                background-color: 2d2d2d;
                border: 1px solid #d3d3d3; 
                border-radius: 5px;                                
            }               
            QPushButton:disabled {
                background-color: 2d2d2d;
                border: 1px solid 222222; 
                border-radius: 5px;                                
            }
        """)

        self.setFocus()


    def keyPressEvent(self, event: QKeyEvent):
        if event.key() == Qt.Key.Key_Escape:
            event.ignore()
            self.closeEvent(QCloseEvent()) # I want to have the save prompt if the user presses the escape key


    def closeEvent(self, event: QCloseEvent):
        if self.close_tab(0):
            event.accept()
        else:
            event.ignore()


    def closing_message(self):
        messageBox = QMessageBox(self)
        messageBox.setWindowTitle(WARNING_UNSAVED_TRANSLATE)
        messageBox.setText(CLOSE_MESSAGE)
        messageBox.setIcon(QMessageBox.Icon.NoIcon)  # Remove the icon
        messageBox.setStandardButtons(QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No | QMessageBox.StandardButton.Cancel)
        
        # Change the text of the buttons
        yes_button = messageBox.button(QMessageBox.StandardButton.Yes)
        yes_button.setText(YES_TRANSLATE)
        no_button = messageBox.button(QMessageBox.StandardButton.No)
        no_button.setText(NO_TRANSLATE)
        no_button = messageBox.button(QMessageBox.StandardButton.Cancel)
        no_button.setText(CANCEL_TRANSLATE)

        messageBox.setDefaultButton(QMessageBox.StandardButton.Cancel)

        messageBox.setStyleSheet("QPushButton { color: #333; background-color: #ccc; border: none; padding: 5px 10px; }"
                                "QPushButton:hover { background-color: #bbb; }")

        question = messageBox.exec()

        if question == QMessageBox.StandardButton.Yes:
            return 2
        elif question == QMessageBox.StandardButton.No:
            return 1
        else:
            return 0


    def close_tab(self, index):
        if index == 0:
            while self.tabs.count() > 0:
                index = self.tabs.count() - 1
                if not index:
                    self.close()
                    return True
                self.tabs.setCurrentIndex(index)
                widget = self.tabs.widget(index)
                if isinstance (widget, CharacterTab):
                    if widget.saved:
                        self.tabs.removeTab(index)
                    else:
                        output = self.closing_message()
                        if output:
                            if output == 2:
                                if not widget.save():
                                    return False
                            self.tabs.removeTab(index)
                        else:
                            return False
                else:
                    self.tabs.removeTab(index)
            self.close()
            return True
        else:
            self.tabs.setCurrentIndex(index)
            widget = self.tabs.widget(index)
            if isinstance (widget, CharacterTab):
                if widget.saved:
                    self.tabs.removeTab(index)
                else:
                    output = self.closing_message()
                    if output:
                        if output == 2:
                            if not widget.save():
                                return False
                        self.tabs.removeTab(index)
            else:
                self.tabs.removeTab(index)
            return False


    def new_character_tab(self, generator = False, character_info = DEFAULT_CHARACTER_INFO):
        tab = CharacterTab(generator, character_info)
        
        # Add the tab to the tabs
        self.tabs.addTab(tab, GENERATOR_WINDOW)

if __name__ == "__main__": # Not really needed, but I'll leave it for now
    app = QApplication(sys.argv)

    # Probably not the best way to resize the window... But it's the best idea I've come up with so far
    scale_factor = 1

    # Apply the scaling factor to the stylesheet
    app.setStyleSheet(f"""
        * {{
            font-size: {int(12 * scale_factor)}px;
        }}
    """)

    mainWindow = MyApp()
    mainWindow.new_character_tab(True)
    mainWindow.show()

    sys.exit(app.exec())


### MIGLIORAMENTI ### entro 12/24

# NUOVE FUNZIONALITÀ
# interfaccia per accedere a varie sezioni
# mostri
# aspetto estetico
# tiri per condizioni + condizioni attuali
# sistemare livelli talenti e impedire si superi una certa soglia di punti. Aggiungere talenti extra (possibilità di inserire talenti aggiuntivi)
# punti ferita temporanei
# dado vita
# denaro
# armi pesanti
# copertura
# armi versatili
# abilità extra talenti
# ispirazione eroica
# riposi brevi e lunghi
# affaticamento
# competenza e uso strumenti
# long rest resets hit point maximum, ability score reduction and exhaustion
# lancio incantesimi con livello maggiore
# preparare incantesimi
# currency conversion
# morte
# save advantages
# no spell se non competenza armature

# QUALITÀ DI VITA
# aggiungere specifiche categorie di specie (es.: umanoidi, taglia, solo base)
# possibilità di comprimere alcune finestre
# sistemare dimensioni relative
# better differentiate which tab is active
# aggiungere tooltip
# automatic BASE_PATH
# scrollbar brutta, da sistemare
# iniziativa automatica (per mostri)
# avviso se si inserisce valore sbagliato
# avviso prima di levelUp
# calcolo peso trasportabile/trasportato
# riaggiungere freccette per nuovo equipaggiamento (va risolto il problema per cui inserisce l'oggetto due volte)
# aggiungere dizionario al ToolTipItemDelegate
# mostra dado di tiro e bonus facendo mouseover su dado
# PF aggiuntivi a livello devono essere almeno 1
# taglia modificabile
# custom abilities che si aggiorni in base alla classe quando non è selezionata
# chiudi tutto senza salvare
# ordinare elementi comboBox
# memorizza oggetti equipaggiati
# danno non può essere negativo
# scegliere tipo danno arma improvvisata
# riposo breve sblocca possibilità di usare dadi vita
# dizionario per buttons
# cosa succede se finiscono le competenze disponibili?
# elementi da riempire di colore diverso (es.: comboBox su casuale)
# salvare se abilità attiva è utilizzabile o meno (forse intendevo "in uso?", devo controllare)
# togliere duplicati da resistenze, ecc.
# dado più coreografico
# personaggio risulta modificato anche se non lo è. Evidentemente viene richiamato erroneamente il metodo dell'*
# link in dizionario non rimanda a stesso elemento

# FILL-IN
# oggetti
# flavour a pagina 39/40
# equipaggiamento iniziale a livelli più alti
# trinket (pagina 46)
# minimo di 1 per dado vita
# abilità attive specie
# features talenti da magic initiate in poi
# niente magie in Ira


### FUTURO ### entro 12/25

# NUOVE FUNZIONALITÀ
# multiclassing
# mix lignaggi/specie (eredità dai genitori) + schede parenti (usare tabelle di Xanathar)(definire funzione per vari parametri)
# generatore/caricatore di mappe
# simulazione combattimento
# magia
# app su telefono
# armi +1/elementali
# rendere utili borse nell'inventario. Drag and drop
# tasto destro per varie opzioni (come rimuovere oggetto o creare una borsa)
# rivendita oggetti
# sistemazione armatura di taglia sbagliata/rovinata
# sotto-sottorazze (es: sottorazze umano)
# allineamento che influenzi caratteristiche di lore
# tiri abilità con altre caratteristiche (es: storia(forza)). Probabilmente con tasto destro
# concentrazione

# QUALITÀ DI VITA
# add other image formats
# cambiare icona app
# image and txt saved as one file
# riordinare equipaggiamento
# drag and drop per le tab (eventualmente scrollbar orizzontale? Devo valurare)
# introdurre f-string?
# voci dizionario con nome di default
# non più di una copia della stessa voce del dizionario (se già presente, apri quella)
# LineEdit con possibilità di cliccare parole (e avere prima un tooltip)

# FILL-IN
# sottoclassi
# classi
# nomi d'infanzia (elfi)
# soprannomi (goliath)
# molteplici nomi (gnomi)