import App from "./App";
import AuthenticationScreen from "./AuthenticationScreen";
import DashboardScreen from "./DashboardScreen";

const routes = [
    {
        path: "/",
        Component: App,
        children: [
            { index: true, Component: DashboardScreen },
            { path: "auth", Component: AuthenticationScreen },
        ],
    },
];

export default routes;
