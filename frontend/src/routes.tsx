import App from "./App";
import AuthenticationScreen from "./AuthenticationScreen";

const routes = [
    {
        path: "/",
        Component: App,
    },
    {
        path: "/auth",
        Component: AuthenticationScreen,
    },
];

export default routes;
