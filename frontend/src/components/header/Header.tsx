import {useDisclosure} from '@mantine/hooks';
import {Link} from 'react-router-dom';
import classes from './Header.module.css';
import {ColorSchemeToggle} from "../colorscheme/ColorSchemeToggle.tsx";
import {Logo} from "../logo/Logo.tsx";
import {useUser} from '../../context/UserContext';
import {useContext} from 'react';
import AppContext from '../../context/AppContext';
import { ShoppingCart } from 'lucide-react';
import { ShoppingCart as ShoppingCartModal } from '../shoppingCart/shoppingCart.tsx';
import { Burger, Button, Divider, Drawer, Group, ScrollArea, ActionIcon, Indicator } from '@mantine/core';

export function Header() {
    const [drawerOpened, { toggle: toggleDrawer, close: closeDrawer }] = useDisclosure(false);
    const { currentUser, toggleUser } = useUser();
    const { state } = useContext(AppContext);
    const { myListsHref, libraryHref } = state.app;
    const [cartOpened, { open: openCart, close: closeCart }] = useDisclosure(false);
    const cartCount = state.cart?.items?.length ?? 0;
    const isLoggedIn = currentUser !== 'guest@aeiou.com';

    return (
        <>
            <header className={classes.header}>
                <Group justify="space-between" h="100%">

                    <Link to="/" aria-label="Go to home">
                        <Logo />
                    </Link>

                    <Group h="100%" gap={0} visibleFrom="sm">
                        <Link to="/" className={classes.link}>MARKETPLACE</Link>
                        {isLoggedIn && libraryHref && <Link to="/my-library" className={classes.link}>LIBRARY</Link>}
                        {isLoggedIn && myListsHref && <Link to="/my-lists" className={classes.link}>LISTS</Link>}
                    </Group>

                    <Group visibleFrom="sm">
                        <ColorSchemeToggle />

                        <Indicator label={cartCount} size={16} disabled={cartCount === 0}>
                            <ActionIcon
                                variant="subtle"
                                size="lg"
                                radius="xl"
                                color="light-dark(var(--mantine-color-black), var(--mantine-color-white))"
                                aria-label="Shopping cart"
                                onClick={openCart}
                                styles={{
                                    root: {
                                        '--ai-hover': 'light-dark(var(--mantine-color-indigo-7), var(--mantine-color-dark-6))',
                                    }
                                }}
                            >
                                <ShoppingCart size={20} />
                            </ActionIcon>
                        </Indicator>

                        <Button color="var(--mantine-color-indigo-7)" radius="xl" onClick={toggleUser}>
                            {isLoggedIn ? 'LOG OUT' : 'LOG IN'}
                        </Button>
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
                    <Link to="/" className={classes.link}>Marketplace</Link>
                    {isLoggedIn && libraryHref && <Link to="/my-library" className={classes.link}>My Library</Link>}
                    {isLoggedIn && myListsHref && <Link to="/my-lists" className={classes.link}>My Lists</Link>}
                    <Divider my="sm" />
                    <Group justify="center" grow pb="xl" px="md">
                        <Button onClick={toggleUser}>
                            {isLoggedIn ? 'LOG OUT' : 'LOG IN'}
                        </Button>
                    </Group>
                </ScrollArea>

                <ActionIcon variant="subtle" size="lg" onClick={() => { closeDrawer(); openCart(); }}>
                    <ShoppingCart size={20} />
                </ActionIcon>

            </Drawer>
            <ShoppingCartModal opened={cartOpened} onClose={closeCart} />

        </>
    );
}
