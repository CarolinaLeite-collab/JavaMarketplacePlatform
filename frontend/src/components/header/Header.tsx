import {
    Burger,
    Button,
    Divider,
    Drawer,
    Group,
    ScrollArea,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { Link } from 'react-router-dom';
import classes from './Header.module.css';
import { ColorSchemeToggle } from "../colorscheme/ColorSchemeToggle.tsx";
import { Logo } from "../logo/Logo.tsx";


export function Header() {
    const [drawerOpened, { toggle: toggleDrawer, close: closeDrawer }] = useDisclosure(false);

    return (
        <>
            <header className={classes.header}>
                <Group justify="space-between" h="100%">

                    <Link to="/" aria-label="Go to home">
                        <Logo />
                    </Link>

                    <Group h="100%" gap={0} visibleFrom="sm">
                        <Link to="/" className={classes.link}>SALES</Link>
                        <Link to="/my-library" className={classes.link}>LIBRARY</Link>
                        <Link to="/my-lists" className={classes.link}>LISTS</Link>
                    </Group>

                    <Group visibleFrom="sm">
                        <ColorSchemeToggle />
                        <Button color="indigo" radius="xl">LOG OUT</Button>
                    </Group>

                    <Burger
                        opened={drawerOpened}
                        onClick={toggleDrawer}
                        hiddenFrom="sm"
                        aria-label="Toggle navigation"
                    />
                </Group>
            </header>

            <Drawer
                opened={drawerOpened}
                onClose={closeDrawer}
                size="100%"
                padding="md"
                title="MiteLovers"
                hiddenFrom="sm"
                zIndex={1000000}
            >
                <ScrollArea h="calc(100vh - 80px)" mx="-md">
                    <Divider my="sm" />
                        <Link to="/" className={classes.link}>My Sales</Link>
                        <Link to="/my-library" className={classes.link}>My Library</Link>
                        <Link to="/my-lists" className={classes.link}>My Lists</Link>
                        {/*<Link to="/account" className={classes.link}>Account</Link>*/}
                    <Divider my="sm" />
                    <Group justify="center" grow pb="xl" px="md">
                        <Button>Log Out</Button>
                    </Group>
                </ScrollArea>
            </Drawer>
        </>
    );
}