#!/bin/bash

echo "🐳 ShedLock Demo - Docker Multi-Instance Setup"
echo "================================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}❌ Docker is not running. Please start Docker Desktop first.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker is running${NC}"
}

# Function to build and start services
start_services() {
    echo -e "${BLUE}🔨 Building and starting services...${NC}"
    docker-compose up --build -d
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Services started successfully!${NC}"
    else
        echo -e "${RED}❌ Failed to start services${NC}"
        exit 1
    fi
}

# Function to show service status
show_status() {
    echo ""
    echo -e "${BLUE}📊 Service Status:${NC}"
    docker-compose ps
    
    echo ""
    echo -e "${BLUE}🌐 Application URLs:${NC}"
    echo -e "${GREEN}Instance 1:${NC} http://localhost:8080"
    echo -e "${GREEN}Instance 2:${NC} http://localhost:8081"
    echo -e "${GREEN}Instance 3:${NC} http://localhost:8082"
    echo -e "${GREEN}MySQL:${NC} localhost:3306"
    
    echo ""
    echo -e "${BLUE}🔍 Health Check URLs:${NC}"
    echo -e "${GREEN}Instance 1 Health:${NC} http://localhost:8080/actuator/health"
    echo -e "${GREEN}Instance 2 Health:${NC} http://localhost:8081/actuator/health"
    echo -e "${GREEN}Instance 3 Health:${NC} http://localhost:8082/actuator/health"
    
    echo ""
    echo -e "${BLUE}📋 EOD Status URLs:${NC}"
    echo -e "${GREEN}Instance 1 EOD:${NC} http://localhost:8080/eod/status"
    echo -e "${GREEN}Instance 2 EOD:${NC} http://localhost:8081/eod/status"
    echo -e "${GREEN}Instance 3 EOD:${NC} http://localhost:8082/eod/status"
}

# Function to show logs
show_logs() {
    echo -e "${BLUE}📜 Recent logs:${NC}"
    docker-compose logs --tail=20
}

# Function to stop services
stop_services() {
    echo -e "${YELLOW}🛑 Stopping services...${NC}"
    docker-compose down
    echo -e "${GREEN}✅ Services stopped${NC}"
}

# Function to clean up
cleanup() {
    echo -e "${YELLOW}🧹 Cleaning up Docker resources...${NC}"
    docker-compose down -v --remove-orphans
    docker system prune -f
    echo -e "${GREEN}✅ Cleanup completed${NC}"
}

# Main script logic
case "${1:-start}" in
    "start")
        check_docker
        start_services
        show_status
        echo ""
        echo -e "${GREEN}🎉 Setup complete! Your multi-instance ShedLock demo is running.${NC}"
        echo ""
        echo -e "${YELLOW}💡 Tips:${NC}"
        echo "  • Use 'docker-compose logs -f' to watch logs"
        echo "  • Use 'docker-compose down' to stop services"
        echo "  • Use './docker-start.sh logs' to see recent logs"
        echo "  • Use './docker-start.sh stop' to stop services"
        echo "  • Use './docker-start.sh cleanup' to clean up everything"
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        stop_services
        sleep 2
        start_services
        show_status
        ;;
    "logs")
        show_logs
        ;;
    "status")
        show_status
        ;;
    "cleanup")
        cleanup
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|logs|status|cleanup}"
        echo ""
        echo "Commands:"
        echo "  start    - Build and start all services (default)"
        echo "  stop     - Stop all services"
        echo "  restart  - Restart all services"
        echo "  logs     - Show recent logs"
        echo "  status   - Show service status and URLs"
        echo "  cleanup  - Stop services and clean up Docker resources"
        exit 1
        ;;
esac 