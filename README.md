# TestFramework
. The hub and nodes are shown here running on the same machine, but of course you can copy the selenium-server-standalone to multiple machines. Note: The selenium-server-standalone package includes the Hub, WebDriver, and legacy RC needed to run the grid. Ant is not required anymore. You can download the selenium-server-standalone-*.jar from http://selenium-release.storage.googleapis.com/index.html This walk-through assumes you already have Java installed.
Step 1: Start the hub

The Hub is the central point that will receive all the test request and distribute them the the right nodes.

Open a command prompt and navigate to the directory where you copied the selenium-server-standalone file. Type the following command:

java -jar selenium-server-standalone-<version>.jar -role hub
The hub will automatically start-up using port 4444 by default. To change the default port, you can add the optional parameter -port when you run the command. You can view the status of the hub by opening a browser window and navigating to: http://localhost:4444/grid/console

Step 2: Start the nodes

Regardless on whether you want to run a grid with new WebDriver functionality, or a grid with Selenium 1 RC functionality, or both at the same time, you use the same selenium-server-standalone jar file to start the nodes.

java -jar selenium-server-standalone-<version>.jar -role node  -hub http://localhost:4444/grid/register
Note: The port defaults to 5555 if not specified whenever the "-role" option is provided and is not hub.

For backwards compatibility "wd" and "rc" roles are still a valid subset of the "node" role. But those roles limit the types of remote connections to their corresponding API, while "node" allows both RC and WebDriver remote connections.

For WebDriver nodes, you will need to use the RemoteWebDriver and the DesiredCapabilities object to define which browser, version and platform you wish to use. Create the target browser capabilities you want to run the tests against:

DesiredCapabilities capability = DesiredCapabilities.firefox();
Pass that into the RemoteWebDriver object:

WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);


A node matches if all the requested capabilities are met. To request specific capabilities on the grid, specify them before passing it into the WebDriver object.

capability.setBrowserName();
capability.setPlatform();
capability.setVersion()
capability.setCapability(,);

