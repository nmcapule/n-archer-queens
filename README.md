# N-Archers

This is an implementation of n-queens solver, but with additional condition that
there should be no 3 or more queens that align in a straight line.

## How to use?

For Linux users, run the app with: `./gradlew run`

## Notes

### Initial attempt

I am currently developing on VS Code remote, with Linux as a host. I prefer not
to install anything so I set up a docker command just to invoke gradle:

```Makefile
docker-gradle:
	docker run --rm -u gradle \
		-v "$(PWD)":/home/gradle/project \
		-w /home/gradle/project \
		gradle gradle $(ARGS)
```

### Setup w/ VS Code

Unfortunately, VS Code's extension for Java really requires a locally installed
JDK, so I had no choice but to use my Linuxbrew to install the latest OpenJDK:

```sh
$ brew install openjdk
```

This is not enough, since VS Code's Java extension can't detect Linuxbrew's
OpenJDK automatically. I had to find out where JDK home is with:

```sh
$ dirname $(dirname $(readlink -f $(which javac)))
```

And put it into my `java.home` settings in VS Code:

```json
{
  "java.home": "/home/linuxbrew/.linuxbrew/Cellar/openjdk/15.0.1/libexec"
}
```

The VS Code Java extension has a built-in auto-formatter. That's neat. It only
activates if the Java Language Server is set to Standard mode instead of
Lightweight mode however.

### Invocation

Running `gradle` with Docker is really slow. So I tried running `gradlew run`
and it installed `gradle` locally anyways -- at-least it's not system-wide
(inside `$PWD/gradle` folder).

Now I could run gradle commands either with:

```sh
# Using gradlew
$ gradlew run
# Using docker
$ make docker-gradle ARGS="run"
```

Pretty neat to explore actually. By the way, `gradlew` still needs a Java
install. If you only got Docker, just use the Docker invocation.
