# login-logger

A system to log all user logins.

It currently supports CraftBukkit, Spigot, and Paper.

It uses ORMLite by default, which gives a variety of databases to use.

## How it works
The system will listen for user logins. When that happens, the system will record the user's id, ip, and login time.

This logger has an "end fallback" system that is for when the server crashes, for example. Without this system, when the
server crashes, the logger will not be able to assign the logout time, which will make the login session to be considered 
active even if the user relogs. The "end fallback" system fixes this by recording a fallback time. When the user logs in,
the logger will assign that time to be (now + time specified in the configuration), and will update the fallback time 
accordingly to the delay specified by the config. If that delay is set to 1 minute, for example, in each minute all 
current sessions will have their "end fallback" time updated to now. That way, when the user relogs, the logger system 
will check for any active session and end them by setting their logout time as the "end fallback" time.