echo Running Maven clean verify...
call mvn test -P test

echo Opening JaCoCo report...
start "" "target\site\jacoco\index.html"

echo Done.
pause
