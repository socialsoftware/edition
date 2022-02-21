import { useLayoutEffect, useState } from 'react';
import ReactTooltip from 'react-tooltip';
import { getReadingExperts } from '../../../api/reading-api';
import { messages } from '../../../resources/constants';
import { getLanguage } from '../../../store';
import ReadingColumn from '../components/ReadingColumn';

export default () => {
  const [experts, setExperts] = useState();

  useLayoutEffect(() => {
    getReadingExperts().then(({ data }) => setExperts(data));
  }, []);

  return (
    <div className="row">
      <div className="main-content">
        <div className="row reading-grid">
          <div
            className="reading__text col-xs-12 col-sm-7 no-pad style-point reading-book-title"
            style={{ height: '155px' }}
          >
            <h1>{messages[getLanguage()]['book_disquiet']}</h1>
          </div>
          {experts &&
            experts.map(({ editor }, index) => (
              <ReadingColumn key={index} editor={editor} />
            ))}
          <div
            className="reading__column col-xs-12 col-sm-1 no-pad recommendation-line"
            style={{ height: '162px' }}
          >
            <h4 className="f--condensed">
              <a href="#" className="f--condensed--link">
                {messages[getLanguage()]['general_recommendation']}
              </a>
              <span className="visible-xs-inline">&nbsp;</span>
              <ReactTooltip
                type="light"
                place="bottom"
                effect="solid"
                className="reading-tooltip"
                border={true}
              />
              <span
                data-tip={messages[getLanguage()]['reading_tt_recom']}
                className="glyphicon glyphicon-info-sign"
              ></span>
            </h4>
          </div>
        </div>
      </div>
    </div>
  );
};
