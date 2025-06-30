#!/bin/bash

echo "Starting ShedLock Demo Application..."
echo "====================================="
echo ""
echo "The application will:"
echo "- Start on http://localhost:8080"
echo "- Use H2 in-memory database"
echo "- Run scheduled job at 11:30 PM EST, Monday to Friday"
echo "- Provide REST endpoint at /eod/status"
echo "- Enable H2 console at /h2-console"
echo ""
echo "To test the application:"
echo "1. Check EOD status: curl http://localhost:8080/eod/status"
echo "2. Access H2 console: http://localhost:8080/h2-console"
echo "3. JDBC URL: jdbc:h2:mem:testdb"
echo "4. Username: sa"
echo "5. Password: (empty)"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

mvn spring-boot:run 