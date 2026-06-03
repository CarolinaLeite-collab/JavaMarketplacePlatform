import { Text } from '@mantine/core';
import classes from './Footer.module.css';

export function Footer() {
    return (
        <div className={classes.footer}>
            <div className={classes.inner}>
                <Text size="sm" c="dimmed">
                    © 2026 Cooperativa de Código da Asprela
                </Text>
            </div>
        </div>
    );
}