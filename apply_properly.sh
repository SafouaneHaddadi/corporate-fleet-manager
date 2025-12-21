#!/bin/bash
echo "=== APPLICATION PROPRE DES COMMITS ==="

# Commits dans l'ordre
commits=(
    "1769b9e" "5e9a2e8" "19c8578" "e039233" "5d9d75b" 
    "bef8148" "29c4737" "b1f2c8e" "fa0a6b1" "ebb2654" 
    "38b8371" "93cb001" "7e70b59" "45251a3" "1c5a850" 
    "734d12b" "60005d7" "f092958" "21f13c2" "03d6674" "d1fec0b"
)

for commit in "${commits[@]}"; do
    echo ""
    echo "--- Commit: $commit ---"
    
    # Essaie normal d'abord
    if git cherry-pick $commit --no-commit 2>/dev/null; then
        echo "✓ Cherry-pick réussi"
        git add -A
        git commit -C $commit
    else
        # Sinon, récupère juste les fichiers
        echo "⚠ Récupération manuelle des fichiers..."
        git cherry-pick --abort 2>/dev/null
        
        # Liste les fichiers du commit
        files=$(git show $commit --name-only --oneline | tail -n +2)
        for file in $files; do
            if [ -f "$file" ]; then
                echo "  Mise à jour: $file"
                git checkout $commit -- "$file" 2>/dev/null
            else
                echo "  Création: $file"
                git checkout $commit -- "$file" 2>/dev/null
            fi
        done
        
        git add -A
        git commit -m "$(git show -s --format='%s' $commit 2>/dev/null || echo "Commit $commit")"
        echo "✓ Appliqué manuellement"
    fi
done

echo ""
echo "=== TERMINÉ ==="
