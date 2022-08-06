(() => {
  const URL = '/ldod-mfes/style/fonts';
  const loader = () =>
    [
      {
        family: 'Work-Sans',
        src: `url('${URL}/work-sans/WorkSans-ExtraBold.ttf') format('truetype')`,
        style: 'normal',
        weight: 800,
        display: 'swap',
      },
      {
        family: 'Space-Mono',
        src: `url('${URL}/space-mono/SpaceMono-Bold.ttf') format('truetype')`,
        style: 'normal',
        weight: 700,
        display: 'swap',
      },
      {
        family: 'Ultra',
        src: `url('${URL}/ultra/Ultra-Regular.ttf') format('truetype')`,
        style: 'normal',
        weight: 400,
        display: 'swap',
      },
      {
        family: 'League-Gothic',
        src: `url('${URL}/league-gothic/LeagueGothic-Regular.otf') format('truetype')`,
        style: 'normal',
        weight: 400,
        display: 'swap',
      },
      {
        family: 'Work-Sans',
        src: `url('${URL}/work-sans/WorkSans-SemiBold.ttf') format('truetype')`,
        style: 'normal',
        weight: 600,
        display: 'swap',
      },
      {
        family: 'Space-Mono',
        src: `url('${URL}/space-mono/SpaceMono-Regular.ttf') format('truetype')`,
        style: 'normal',
        weight: 400,
        display: 'swap',
      },
    ].forEach(({ family, src, style, weight, display }) => {
      let font = new FontFace(family, src, {
        style,
        weight,
        display,
      });
      font
        .load()
        .then((loaded) => document.fonts.add(loaded))
        .catch((e) => console.log(e.message));
    });

  window.onmousemove = loader;
})();
