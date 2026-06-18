import { Tabs } from "@mantine/core";
import { useNavigate, useLocation } from "react-router-dom";

export function ListsTabs() {
    const navigate = useNavigate();
    const location = useLocation();

    const active = location.pathname.startsWith("/lists/public")
        ? "public"
        : "my-lists";

    return (
        <Tabs
            value={active}
            onChange={(value) => {
                if (value === "public") navigate("/lists/public");
                if (value === "my-lists") navigate("/lists/my-lists");
            }}
            mb="md"
        >
            <Tabs.List justify="center">
                <Tabs.Tab value="my-lists">My lists</Tabs.Tab>
                <Tabs.Tab value="public">Public lists</Tabs.Tab>
            </Tabs.List>
        </Tabs>
    );
}
