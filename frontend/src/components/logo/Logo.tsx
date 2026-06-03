import { useComputedColorScheme } from '@mantine/core';
import LogoLight from '../../assets/MiteloversLogoBlack.svg';
import LogoDark from '../../assets/MiteloversLogoWhite.svg';

export function Logo() {

    const colorScheme = useComputedColorScheme('light');

    return (
        <img
            src={colorScheme === 'light' ? LogoLight : LogoDark}
            alt="Mitelovers Logo"
            height={40}
        />
    );
}