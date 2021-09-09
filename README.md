# lCore
Mongo & Redis Based Rank/Punishment/Essential Core, below are my todo list.

- Make Permission Attachments for players
- Start a basis of Punishments
- Start on essential commands

***

### Introduction

My name is LBuddyBoy or Ethan and I have started a base of a rank core that I will be working on as much as I can and this will be a fun project for me and hopefully helpful for anything that wants to use it.

### config.yml (current)

```
mongo:
  enabled: true
  host: "localhost"
  port: 27017
  database: "lCore"
  auth:
    enabled: false
    username: ""
    password: ""
    auth-db: ""

use-yaml-storage: false # You can only use one at once.

server-name: "lCore"

use-protected-ip-storage: true
failed-to-load-profile: "&cFailed to load your profile. Contact an admin."

grant:
  grants:
    menu-title: "&6Grants: %player%"
    button:
      name: "&6%rank%"
      lore:
        - ''
        - '&6Added By&7: &f%addedBy%'
        - '&6Added At&7: &f%addedAt%'
        - '&6Duration&7: &f%duration%'
        - '&6Reason&7: &f%addedAt%'
        - '&6Time Left&7: &f%time-left%'
        - ''
      lore-removed:
        - ''
        - '&6Added By&7: &f%addedBy%'
        - '&6Added At&7: &f%addedAt%'
        - '&6Duration&7: &f%duration%'
        - '&6Reason&7: &f%addedAt%'
        - '&6Time Left&7: &f%time-left%'
        - ''
        - '&6Removed At&7: &f%removedAt%'
        - '&6Removed By&7: &f%removedBy%'
        - '&6Removed For&7: &f%removedFor%'
        - ''
  granted:
    expired: "&aYour %rank% has just expired&a."
    sender: "&aYou have just granted %player% &athe %rank%&a for &e%time%&a."
    target: "&aYou have just been granted &athe %rank%&a for &e%time%&a."

punish:
  blacklist:
    kick-message: "&cYour account is blacklisted from the Example Network\n&cThis punishment cannot be appealed\n"
  mute:
    message:
      - '&cYou have been muted for %reason%'
      - '&cExpires: %time-left%'
  ban:
    kick-message: "&cYour account is banned from the Example Network.\n%temp-format%\n\n&cIf you feel this punishment is unjust, you may appeal at:\nhttps://www.lbuddyboy.me"
    temporary-format: "&cExpires: %time%"
  kick:
    kick-message: "&cYou have been kicked from the server.\n&cReason: %reason%"

rank:
  edit:
    menu-title: "&6Editing: &r%rank%"
  non-existant: "&cThat rank does not exist"
  exists: "&cThat rank already exists"
  deleted: "&aSuccessfully created the %rank%&a."
  created: "&aSuccessfully deleted the %rank%&a."
  rename: "&aSucessfully renamed the %rank% to %new%"
  setPrefix: "&aSucessfully set the prefix of %rank% to %new%"
  setDisplay: "&aSucessfully set the displayname of %rank% to %new%"
  setWeight: "&aSucessfully set the weight of %rank% to %new%"
  setColor: "&aSucessfully set the color of %rank% to %new%"
  addPerm: "&aSucessfully added the %perm% permission to %rank%"
  removePerm: "&aSucessfully removed the %perm% permission from %rank%"
  hasPerm: "&cThat rank already has that permission."
  noPerm: "&cThat rank does not have that permission."
  list:
    header:
      - " "
      - "&6&lRank Info"
      - " "
    format: "&7%right_arrow%&r %rank% &7(Weight: %weight%)"
```

### Contact Me

- Discord - LBuddyBoy#6163
- [Telegram](https://t.me/LBuddyBoy)
- [Twitter](https://twitter.com/LBuddyBoy)
- [Webiste](https://lbuddyboy.me)