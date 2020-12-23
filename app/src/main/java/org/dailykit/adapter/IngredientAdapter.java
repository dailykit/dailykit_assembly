package org.dailykit.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.activity.DashboardActivity;
import org.dailykit.activity.SingleScanActivity;
import org.dailykit.fragment.IngredientFragment;
import org.dailykit.model.ScanIngredientDataModel;
import org.dailykit.R;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.database.GroctaurantExecutor;
import org.dailykit.room.entity.IngredientDetailEntity;
import org.dailykit.room.entity.IngredientEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;
import org.dailykit.util.SoftwareConfig;

import java.util.List;

import timber.log.Timber;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.SingleItemRowHolder> {

    public static final String TAG = "IngredientAdapter";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int currentPosition;
    IngredientInternalAdapter adapter;
    private ItemEntity itemEntity;
    GroctaurantDatabase groctaurantDatabase;
    GroctaurantExecutor groctaurantExecutor;
    private DashboardActivity activity;
    private List<IngredientEntity> ingredientEntityList;
    private List<IngredientDetailEntity> ingredientDetailEntityList;
    boolean fullyPacked=false;
    IngredientFragment ingredientFragment;
    private SoftwareConfig softwareConfig;
    int numberOfPackedIngredients;
    boolean packingEnabled = true;

    public IngredientAdapter(Activity activity, IngredientFragment ingredientFragment, List<IngredientEntity> ingredientEntityList, ItemEntity itemEntity,int numberOfPackedIngredients) {
        this.ingredientEntityList = ingredientEntityList;
        this.ingredientFragment = ingredientFragment;
        this.activity =(DashboardActivity)activity;
        this.itemEntity = itemEntity;
        groctaurantDatabase = Room.databaseBuilder(activity, GroctaurantDatabase.class, "Development")
                .allowMainThreadQueries()
                .build();
        groctaurantExecutor = GroctaurantExecutor.getInstance();
        softwareConfig = new SoftwareConfig(activity);
        this.numberOfPackedIngredients = numberOfPackedIngredients;
        if(!softwareConfig.isPartialPackingEnabled() && numberOfPackedIngredients!=ingredientEntityList.size()){
            this.packingEnabled=false;
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_list, parent,false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final IngredientEntity singleItem = ingredientEntityList.get(position);
        //Timber.e("Packing Enabled : "+packingEnabled);
        Timber.e("Ingredient Name : "+singleItem.getIngredientEntity().get(0).getIngredientName()+" IsPacked :"+singleItem.getIngredientEntity().get(0).isPacked());
        //Timber.e("Adapter Selected Position : "+itemEntity.getSelectedPosition());
        if (itemEntity.getSelectedPosition() == position && groctaurantDatabase.ingredientDao().isScanned(singleItem.getIngredientId()) && singleItem.getIngredientEntity().size() == 1) {
            //Timber.e("Layout 1");
            holder.ingredientLayout.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.ingredientTitle.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientWeight.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientPortioning.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientMoreOption.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_left_black));
            holder.ingredientLessOption.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_right_black));
            holder.ingredientRelabel.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_label_black));
            holder.ingredientReprint.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_print_black));
            holder.ingredientDelete.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_delete_black));
            holder.ingredientRepack.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_repack_black));
        } else if (itemEntity.getSelectedPosition() == position && groctaurantDatabase.ingredientDao().isScanned(singleItem.getIngredientId()) && singleItem.getIngredientEntity().size() > 1) {
            //Timber.e("Layout 2");
            holder.ingredientInnerLayout.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.ingredientTitle.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientWeight.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientPortioning.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientMoreOption.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_left_black));
            holder.ingredientLessOption.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_right_black));
            holder.ingredientRelabel.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_label_black));
            holder.ingredientReprint.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_print_black));
            holder.ingredientDelete.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_delete_black));
            holder.ingredientRepack.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_repack_black));
        } else {
            //Timber.e("Layout 3");
            holder.ingredientLayout.setBackgroundColor(0x00000000);
            holder.ingredientTitle.setTextColor(activity.getResources().getColor(R.color.white));
            holder.ingredientWeight.setTextColor(activity.getResources().getColor(R.color.white));
            holder.ingredientPortioning.setTextColor(activity.getResources().getColor(R.color.white));
            holder.ingredientMoreOption.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_left_arrow));
            holder.ingredientLessOption.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_right_arrow));
            holder.ingredientRelabel.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_label));
            holder.ingredientReprint.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_print));
            holder.ingredientDelete.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_delete));
            holder.ingredientRepack.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_repack));
        }



        ingredientDetailEntityList = groctaurantDatabase.ingredientDetailDao().loadIngredientDetailByIngredientId(singleItem.getIngredientId());

        if(groctaurantDatabase.ingredientDao().isPacked(ingredientDetailEntityList.get(0).getIngredientId())){
            holder.ingredientBetaMainImage.setVisibility(View.VISIBLE);
        }
        else{
            holder.ingredientLayout.setBackgroundColor(activity.getResources().getColor(R.color.black));
            holder.ingredientBetaMainImage.setVisibility(View.GONE);
        }

        String slipName = singleItem.getSlipName().toLowerCase();
        String[] slipNameArray = slipName.split(" ");
        StringBuilder slipNameBuilder = new StringBuilder();
        for (String s : slipNameArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            slipNameBuilder.append(cap + " ");
        }

        String process = ingredientDetailEntityList.get(0).getIngredientProcess().toLowerCase();
        String[] processArray = process.split(" ");
        StringBuilder processBuilder = new StringBuilder();
        for (String s : processArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            processBuilder.append(cap + " ");
        }


        holder.ingredientTitle.setText(singleItem.getIngredientIndex() + ") " + slipNameBuilder.toString());
        holder.ingredientPortioning.setText(processBuilder.toString());
        if (ingredientDetailEntityList.size() == 1) {
            if (singleItem.isScanned()) {
                holder.ingredientAlphaMainImage.setVisibility(View.VISIBLE);
            } else {
                holder.ingredientAlphaMainImage.setVisibility(View.GONE);
            }
            //Timber.e( "Packing Value : " + groctaurantDatabase.ingredientDetailDao().isPackedComplete(singleItem.getIngredientEntity().get(0).getIngredientDetailId()) + " " + singleItem.getIngredientEntity().get(0).isPackedComplete() + " " + singleItem.getIngredientEntity().get(0).getIngredientName());

            //holder.ingredientFullDetailList.setVisibility(View.GONE);
            if (groctaurantDatabase.ingredientDao().isScanned(ingredientDetailEntityList.get(0).getIngredientId())) {
                /*if(singleItem.getIngredientEntity().get(0).isPackedComplete()){*/
                Timber.e(singleItem.getSlipName()+" isScanned");
                holder.ingredientLayout.setBackgroundColor(activity.getResources().getColor(R.color.black));
                holder.ingredientAlphaImage.setVisibility(View.GONE);
                holder.ingredientAlphaMainImage.setVisibility(View.VISIBLE);
            }
            else{
                holder.ingredientAlphaMainImage.setVisibility(View.GONE);
            }

            double totalWeight = 0.0;
            for (IngredientDetailEntity ingredientDetailEntity : ingredientDetailEntityList) {
                totalWeight=totalWeight+ingredientDetailEntity.getIngredientQuantity();
            }
            holder.ingredientWeight.setText( totalWeight+ " gms");

        } else {
            //holder.ingredientFullDetailList.setVisibility(View.VISIBLE);
            fullyPacked = true;
            double totalWeight = 0.0;
            for (IngredientDetailEntity ingredientDetailEntity : ingredientDetailEntityList) {
                //Timber.e("IngredientDetailEntity Position : "+position+ " Value :"+ingredientDetailEntity.toString());
                totalWeight=totalWeight+ingredientDetailEntity.getIngredientQuantity();
                if (!ingredientDetailEntity.isPacked()) {
                    fullyPacked = false;
                }
            }
            holder.ingredientWeight.setText( totalWeight+ " gms");
            if (fullyPacked) {
                //Timber.e("Fully Packed Item : "+singleItem.getSlipName());
                Timber.e(singleItem.getSlipName()+" Fully Packed Item ");
                holder.ingredientAlphaImage.setVisibility(View.GONE);
                holder.ingredientAlphaMainImage.setVisibility(View.VISIBLE);
            }
            else {
                holder.ingredientAlphaMainImage.setVisibility(View.GONE);
            }
            /*final Handler handler = new Handler();
            handler.postDelayed(() -> {
                holder.ingredientFullDetailList.setHasFixedSize(true);
                adapter = new IngredientInternalAdapter(activity, singleItem, position == itemEntity.getSelectedPosition(),itemEntity,position);
                holder.ingredientFullDetailList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.ingredientFullDetailList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }, 100);*/
        }


        holder.ingredientLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(singleItem.isScanned()){
                        Toast.makeText(activity, "Ingredient Already Scanned", Toast.LENGTH_SHORT).show();
                    }
                    else if(!singleItem.isPackedComplete()){
                        Toast.makeText(activity, "Ingredient Not Packed", Toast.LENGTH_SHORT).show();
                    }
                    else if(!packingEnabled){
                        Toast.makeText(activity, "Please wait for packing of all the Ingredients for this Item", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        groctaurantDatabase.itemDao().setSelectedItem(itemEntity.getItemOrderId(), position);
                        ingredientDetailEntityList = groctaurantDatabase.ingredientDetailDao().loadIngredientDetailByIngredientId(singleItem.getIngredientId());
                        if (ingredientDetailEntityList.size() == 1) {
                            editor.putString(Constants.SELECTED_INGREDIENT_ENTITY, new Gson().toJson(ingredientDetailEntityList.get(0)));
                            editor.commit();
                        }
                        itemEntity.setSelectedPosition(position);
                        Timber.e("ChangeIngredientList Called 1");
                        if(softwareConfig.isRapidScanningEnabled()){
                            Intent intent=new Intent(activity, ContinuousScanActivity.class);
                            activity.startActivity(intent);
                        }
                        else{
                            ScanIngredientDataModel scanIngredientDataModel=new ScanIngredientDataModel(slipNameBuilder.toString(),
                                    holder.ingredientWeight.getText().toString(),holder.ingredientPortioning.getText().toString());
                            editor.putString(Constants.SCANNED_INGREDIENT_ENTITY, new Gson().toJson(scanIngredientDataModel));
                            editor.commit();
                            Intent intent=new Intent(activity, SingleScanActivity.class);
                            activity.startActivity(intent);
                        }

                        activity.updateIngredientList();
                    }
                }
            });


        holder.ingredientMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Timber.e( "More Options Clicked");
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        3.0f
                );
                holder.llOptions.setLayoutParams(param);

                param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                holder.llName.setLayoutParams(param);

                param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                holder.llWeight.setLayoutParams(param);
                param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                holder.llPortioning.setLayoutParams(param);
                holder.ingredientMoreOption.setVisibility(View.GONE);
                holder.llOptionRight.setVisibility(View.VISIBLE);
            }
        });

        holder.ingredientLessOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Timber.e( "Less Options Clicked");
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                holder.llOptions.setLayoutParams(param);

                param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2.0f
                );
                holder.llName.setLayoutParams(param);
                holder.llPortioning.setLayoutParams(param);

                param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                holder.llWeight.setLayoutParams(param);
                holder.llOptionRight.setVisibility(View.GONE);
                holder.ingredientMoreOption.setVisibility(View.VISIBLE);
            }
        });


        holder.ingredientDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle("Delete Ingredient")
                        .setMessage("Are you sure you want to delete this ingredient?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                groctaurantDatabase.ingredientDao().isDeletedUpdate(singleItem.getIngredientId(),true);
                                Toast.makeText(activity, "Ingredient Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Timber.e("ChangeIngredientList Called 2");
                                activity.updateIngredientList();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        holder.ingredientReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle("Reprint Label")
                        .setMessage("Are you sure you want to reprint the label for this ingredient?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "Printing Label", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.ingredientRelabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle("Repack Ingredient")
                        .setMessage("Are you sure you want to repack this ingredient?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                groctaurantDatabase.ingredientDao().isPackedCompleteUpdate(singleItem.getIngredientId(),false);
                                groctaurantDatabase.ingredientDetailDao().isPackedUpdateByIngredientId(singleItem.getIngredientId(),false);
                                Toast.makeText(activity, "Unpacked Ingredient", Toast.LENGTH_SHORT).show();
                                Timber.e("ChangeIngredientList Called 3");
                                activity.updateIngredientList();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.ingredientRepack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle("Repack Ingredient")
                        .setMessage("Are you sure you want to repack this ingredient?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "Unpacked Ingredient", Toast.LENGTH_SHORT).show();
                                ingredientFragment.updatePackingStatus(singleItem.getIngredientId());
                                Timber.e("ChangeIngredientList Called 3");
                                activity.updateIngredientList();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != ingredientEntityList ? ingredientEntityList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected RecyclerView ingredientFullDetailList;
        protected TextView ingredientTitle, ingredientWeight, ingredientPortioning;
        protected LinearLayout ingredientLayout, ingredientInnerLayout, llOptions, llName, llWeight, llOptionRight, llPortioning;
        protected ImageView ingredientAlphaImage, ingredientAlphaMainImage, ingredientBetaMainImage , ingredientMoreOption, ingredientLessOption,
                ingredientDelete, ingredientReprint, ingredientRelabel,ingredientRepack;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.ingredientFullDetailList = (RecyclerView) itemView.findViewById(R.id.ingredient_full_detail_list);
            this.ingredientTitle = (TextView) itemView.findViewById(R.id.ingredient_title);
            this.ingredientWeight = (TextView) itemView.findViewById(R.id.ingredient_weight);
            this.ingredientPortioning = (TextView) itemView.findViewById(R.id.ingredient_processing);
            this.ingredientLayout = (LinearLayout) itemView.findViewById(R.id.item_order_layout);
            this.ingredientInnerLayout = (LinearLayout) itemView.findViewById(R.id.ingredient_inner_layout);
            this.ingredientAlphaImage = (ImageView) itemView.findViewById(R.id.ingredient_alpha_image);
            this.ingredientAlphaMainImage = (ImageView) itemView.findViewById(R.id.ingredient_alpha_main_image);
            this.ingredientBetaMainImage = (ImageView) itemView.findViewById(R.id.ingredient_beta_main_image);
            this.ingredientMoreOption = (ImageView) itemView.findViewById(R.id.ingredient_more_options);
            this.ingredientLessOption = (ImageView) itemView.findViewById(R.id.ingredient_less_option);
            this.ingredientDelete = (ImageView) itemView.findViewById(R.id.ingredient_delete);
            this.ingredientReprint = (ImageView) itemView.findViewById(R.id.ingredient_reprint);
            this.ingredientRelabel = (ImageView) itemView.findViewById(R.id.ingredient_relabel);
            this.ingredientRepack = (ImageView) itemView.findViewById(R.id.ingredient_repack);
            this.llOptions = (LinearLayout) itemView.findViewById(R.id.ll_options);
            this.llPortioning = (LinearLayout) itemView.findViewById(R.id.ll_ingredient_processing);
            this.llName = (LinearLayout) itemView.findViewById(R.id.ll_ingredient_name);
            this.llWeight = (LinearLayout) itemView.findViewById(R.id.ll_ingredient_weight);
            this.llOptionRight = (LinearLayout) itemView.findViewById(R.id.ll_option_right);
        }
    }
}

