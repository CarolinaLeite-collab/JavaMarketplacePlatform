import { useComputedColorScheme, useMantineColorScheme, ActionIcon } from '@mantine/core';
import { IconSun, IconMoon } from '@tabler/icons-react';

export function ColorSchemeToggle() {
    const { setColorScheme } = useMantineColorScheme();
    const colorScheme = useComputedColorScheme('light');

    return (
        <ActionIcon
            onClick={() => setColorScheme(colorScheme === 'light' ? 'dark' : 'light')}
            variant="default"
            size="lg"
            radius="xl"
            aria-label="Toggle color scheme"
        >
            {colorScheme === 'light' ? <IconMoon size={18} /> : <IconSun size={18} />}
        </ActionIcon>
    );
}