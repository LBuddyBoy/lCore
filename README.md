# lCore
Mongo & Redis Based Rank/Punishment/Essential Core, below are my todo list.

- Make Permission Attachments for players
- Start a basis of Punishments
- Start on essential commands

***

### Introduction

My name is LBuddyBoy or Ethan and I have started a base of a rank core that I will be working on as much as I can and this will be a fun project for me and hopefully helpful for anything that wants to use it.

### Information

- Version | (Only tested on 1.7-1.8)
- Configurable
- Optimized

### config.yml (current)

```
mongo:
  host: "localhost"
  port: 27017
  database: "lCore"


failed-to-load-profile: "&cFailed to load your profile. Contact an admin."

grant:
  granted:
    sender: "&aYou have just granted %player% &athe %rank%&a for &e%time%&a."
    target: "&aYou have just been granted &athe %rank%&a for &e%time%&a."

rank:
  non-existant: "&cThat rank does not exist"
  exists: "&cThat rank already exists"
  deleted: "&aSuccessfully created the %rank%&a."
  created: "&aSuccessfully deleted the %rank%&a."
  list:
    header:
      - " "
      - "&6&lRank Info"
      - " "
    format: "&7%right_arrow%&r %rank%"
```

### Contact Me

- Discord - LBuddyBoy#6163
- [Telegram](https://t.me/LBuddyBoy)
- [Twitter](https://twitter.com/LBuddyBoy)
- [Webiste](https://lbuddyboy.me)