# CI and releases

## Local verification

Use JDK 25 and Maven 3.9.16. Run from the repository root:

```sh
mvn -B -ntp clean verify
```

To also build sources and Javadoc without signing or publishing:

```sh
mvn -B -ntp -Prelease "-Dgpg.skip=true" clean verify
```

## Run CI

[CI](https://github.com/allurx/annotation-parser/actions/workflows/ci.yml) runs
automatically on pushes and pull requests to `dev` or `main`.
To start it manually, open **Actions → CI → Run workflow**, select the branch,
and click **Run workflow**.

## Publish a release

Before the first release, complete the
[repository setup](https://github.com/allurx/allurx-build#readme).

1. Update the project version in [pom.xml](../pom.xml) and run both local
   verification commands above.
2. Merge the release changes from `dev` into `main`. Confirm the latest `main`
   push CI run for that exact commit and its latest attempt succeeded.
3. From a clean checkout, synchronize local `main`:

   ```sh
   git switch main
   git pull --ff-only
   git rev-parse HEAD
   ```

4. Confirm `HEAD` matches the successful CI commit. Replace `X.Y.Z` with the
   project version, then create and push an annotated tag:

   ```sh
   git tag -a vX.Y.Z -m "Release vX.Y.Z"
   git push origin vX.Y.Z
   ```

5. Follow the
   [Release workflow](https://github.com/allurx/annotation-parser/actions/workflows/release.yml)
   and check the resulting
   [GitHub Release](https://github.com/allurx/annotation-parser/releases).

For publication checks and failed-run handling, follow the
[shared release guide](https://github.com/allurx/allurx-build/blob/main/docs/release-guide.md).
