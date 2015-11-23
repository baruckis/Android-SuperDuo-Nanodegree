package barqsoft.footballscores.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Andrius-Baruckis on 2015-11-07.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(getApplicationContext(), intent);
    }

}

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        public static final int COL_HOME = 1;
        public static final int COL_AWAY = 2;
        public static final int COL_HOME_GOALS = 3;
        public static final int COL_AWAY_GOALS = 4;

        private static final String[] SCORE_COLUMNS = {
                DatabaseContract.scores_table.DATE_COL,
                DatabaseContract.scores_table.HOME_COL,
                DatabaseContract.scores_table.AWAY_COL,
                DatabaseContract.scores_table.HOME_GOALS_COL,
                DatabaseContract.scores_table.AWAY_GOALS_COL,
                DatabaseContract.scores_table.MATCH_ID,
                DatabaseContract.scores_table.LEAGUE_COL
        };

        private final Context mContext;

        private Cursor data = null;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (data != null) {
                data.close();
            }

            final long identityToken = Binder.clearCallingIdentity();

            Uri dateUri = DatabaseContract.scores_table.buildScoreWithDate();
            String[] date = new String[1];
            date[0] = Utilies.getFragmentDate(0);
            data = mContext.getContentResolver().query(dateUri, SCORE_COLUMNS, DatabaseContract.PATH_DATE, date, null);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION ||
                    data == null || !data.moveToPosition(position)) {
                return null;
            }


            RemoteViews view = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_detail_list_item);

            String homeName = data.getString(COL_HOME);
            String awayName = data.getString(COL_AWAY);
            int homeGoal = data.getInt(COL_HOME_GOALS);
            int awayGoal = data.getInt(COL_AWAY_GOALS);

            view.setTextViewText(R.id.home_name, homeName);
            view.setTextViewText(R.id.away_name, awayName);

            String homeScore = "";
            String awayScore = "";

            if (homeGoal != -1) {
                homeScore += homeGoal;
                awayScore += awayGoal;
            } else {
                homeScore = "-";
                awayScore = "-";
            }

            view.setTextViewText(R.id.home_score, homeScore);
            view.setTextViewText(R.id.away_score, awayScore);

            final Intent intent = new Intent();

            Uri scoreUri = DatabaseContract.scores_table.buildScoreWithDate();

            intent.setData(scoreUri);
            view.setOnClickFillInIntent(R.id.widget_list_item, intent);
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(mContext.getPackageName(), R.layout.widget_detail_list_item);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (data.moveToPosition(position)) return data.getLong(1);
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

//... include adapter-like methods here. See the StackView Widget sample.


}
