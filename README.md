# lCore
Mongo & Redis Based Rank/Punishment/Essential Core, below are my todo list.
The premium version of this is available on http://discord.lbuddyboy.me

- Make Profiling Base for players globally (Done)
- Make Permission Attachments for players (Done)
- Start a basis of Punishments (Done)
- Start on essential commands (WIP)

***

### Introduction

My name is LBuddyBoy or Ethan and I have started a base of a rank core that I will be working on as much as I can and this will be a fun project for me and hopefully helpful for anything that wants to use it.

### config.yml (current)

```
redis:
  channel-id: 1

vault-integration: false
papi-integration: false

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
timezone: "EST"

report-cooldown: "1m"
report-on-cooldown: "&cYou are currently on cooldown for reporting. Try again in %time%"
use-protected-ip-storage: true
failed-to-load-profile: "&cFailed to load your profile. Contact an admin."

grant:
  active-display: "&a&lACTIVE"
  inactive-display: "&c&lINACTIVE"
  grants:
    menu-title: "&6Grants: %player%"
    button:
      name: "&6%rank% &7(%status%&7)"
      lore:
        - ''
        - '&6Added By&7: &f%addedBy%'
        - '&6Added At&7: &f%addedAt%'
        - '&6Duration&7: &f%duration%'
        - '&6Reason&7: &f%reason%'
        - '&6Time Left&7: &f%time-left%'
        - ''
        - '&fClick to remove this grant'
        - ''
      lore-removed:
        - ''
        - '&6Added By&7: &f%addedBy%'
        - '&6Added At&7: &f%addedAt%'
        - '&6Duration&7: &f%duration%'
        - '&6Reason&7: &f%reason%'
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
  alts:
    header:
      - ''
      - '&6&lAlts'
      - ''
    offline: "&7"
    online: "&a"
    muted: "&e"
    banned: "&c"
    blacklisted: "&4"
    info-line: "&7Offline &f- &aOnline &f- &eMuted &f- &cBanned &f- &4Blacklisted"
  blacklist:
    display: "&0&lBlacklist"
    kick-message: "&cYour account is blacklisted from the Example Network\n&cThis punishment cannot be appealed\n"
  mute:
    display: "&6&lMute"
    resolved-message: "&aYour mute has been lifted. You may now chat."
    message:
      - '&cYou have been muted for %reason%'
      - '&cExpires: %time%'
  ban:
    display: "&c&lBan"
    kick-message: "&cYour account is banned from the Example Network.\n%temp-format%\n\n&cIf you feel this punishment is unjust, you may appeal at:\nhttp://www.lbuddyboy.me"
    temporary-format: "&cExpires: %time%"
  kick:
    display: "&e&lKick"
    kick-message: "&cYou have been kicked from the server.\n&cReason: %reason%"
  warn:
    display: "&a&lWarn"
    message:
      - '&cYou have been warned for %reason%'
      - '&cExpires: 7 days'
    warns-before-command: 3
    warn-command: "ban %player% 3d Reached maximum amount of warns"
    warn-last-time: "7d"

reports:
  sent-sender: "&aWe have received your request for help and we will assist you shortly."
  go-to-server-message: "&7(( Click here to go to their server ))" # leave blank to not send the message
  tp-sender-message: "&7(( Click here to go to teleport to the sender ))" # leave blank to not send the message
  tp-target-message: "&7(( Click here to go to teleport to the target ))" # leave blank to not send the message
  report-staff-message:
    - '&6&lREPORT &f- &e%sender%&f needs assistance on &e%server%&f.'
    - '&6Target&f: %target%'
    - '&6Reason&f: %reason%'
  helpop-staff-message:
    - '&6&lHELPOP &f- &e%sender%&f needs assistance on &e%server%&f.'
    - '&6Reason&f: %reason%'
  staff-resolved-message: "&aSuccessfully resolved that report. It has now been moved in to the resolved reports section."
  staff-deleted-message: "&aSuccessfully deleted that report. It can no longer be recovered."
  menu:
    amount-of-reports-button-name: "&6Reports&f: %reports%"
    resolved-title: "&6Reports"
    title: "&6Reports"
    material: "PAPER"
    data: 0
    report-name: "&6&lReport &f#%id%"
    helpop-lore:
      - ''
      - '&6Sent At&f: %sender%'
      - '&6Sent For&f: %reason%'
      - '&6Sent At&f: %sentAt%'
      - '&6Sent On&f: %server%'
      - ''
      - '&fClick here to resolve this report'
      - ''
    report-lore:
      - ''
      - '&6Sent At&f: %sender%'
      - '&6Sent To&f: %target%'
      - '&6Sent For&f: %reason%'
      - '&6Sent At&f: %sentAt%'
      - '&6Sent On&f: %server%'
      - ''
      - '&fClick here to resolve this report'
      - ''
    resolved-helpop-lore:
      - ''
      - '&6Sent At&f: %sender%'
      - '&6Sent For&f: %reason%'
      - '&6Sent At&f: %sentAt%'
      - '&6Sent On&f: %server%'
      - ''
      - '&6Resolved By&f: %resolvedBy%'
      - '&6Resolved At&f: %resolvedAt%'
      - ''
      - '&fClick here to delete this report'
      - ''
    resolved-report-lore:
      - ''
      - '&6Sent At&f: %sender%'
      - '&6Sent To&f: %target%'
      - '&6Sent For&f: %reason%'
      - '&6Sent At&f: %sentAt%'
      - '&6Sent On&f: %server%'
      - ''
      - '&6Resolved By&f: %resolvedBy%'
      - '&6Resolved At&f: %resolvedAt%'
      - ''
      - '&fClick here to delete this report'
      - ''

rank:
  edit:
    menu-title: "&6Editing: &r%rank%"
  non-existant: "&cThat rank does not exist"
  exists: "&cThat rank already exists"
  deleted: "&aSuccessfully deleted the %rank%&a."
  created: "&aSuccessfully created the %rank%&a."
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
